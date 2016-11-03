package com.zok.art.zhihu.ui.refresh;

import android.os.Bundle;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.BeforeStoryBean;
import com.zok.art.zhihu.bean.ListStoryBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.db.RealmManager;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.CastUtil;
import com.zok.art.zhihu.utils.DateUtil;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class RefreshPresenter<M, V extends RefreshContract.View<M>, P>
        implements RefreshContract.Presenter<M, V> {
    private V mView;
    private P mInitParams;

    // latest data and list
    protected M mLatestData;
    private List<ListStoryBean> mListNewsData;

    // URL API Service
    private ApiService mApiService;

    private Subscription mLatestSubscribe;
    private Subscription mBeforeSubscribe;

    private Date mDate;

    public RefreshPresenter(Bundle initParams) {
        if (initParams != null)
            mInitParams = CastUtil.cast(initParams.getParcelable(Constants.EXTRA_INIT_PARAMS));
        mApiService = ApiManager.getApiService();
    }

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void start() {
        updateLatest();
    }

    @Override
    public void updateLatest() {
        if (mLatestSubscribe == null || mLatestSubscribe.isUnsubscribed()) {
            mView.openRefreshUI();
            loadLatestData();
        } else {
            mView.closeRefreshUI();
        }
    }

    @Override
    public void updateBefore() {
        if (mBeforeSubscribe == null || mBeforeSubscribe.isUnsubscribed()) {
            LoadBeforeData();
        }
    }

    @Override
    public void pause() {
        // 如果正在加载，则取消加载并关闭刷新UI
        if (mLatestSubscribe != null && !mLatestSubscribe.isUnsubscribed()) {
            mLatestSubscribe.unsubscribe();
            mView.closeRefreshUI();
        }

        if (mBeforeSubscribe != null && !mBeforeSubscribe.isUnsubscribed()) {
            mBeforeSubscribe.unsubscribe();
        }
    }

    @Override
    public void resume() {
        // 可以选择切换界面重新加载
        // updateLatest();
        mDate = new Date();
    }

    private void loadLatestData() {
        Observable<M> observable = getLatestObservable(mApiService, mInitParams);
        mLatestSubscribe = observable.subscribeOn(Schedulers.io())
                .filter(new Func1<M, Boolean>() {
                    @Override
                    public Boolean call(M m) {
                        return mLatestData == null || !mLatestData.equals(m);
                    }
                }).doOnNext(new Action1<M>() {
                    @Override
                    public void call(M m) {
                        initReadState(getListBeans(m));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<M>() {
                    @Override
                    public void call(M m) {
                        mLatestData = m;
                        mListNewsData = getListBeans(mLatestData);
                        mListNewsData.add(0, getTitleItem());
                        mView.updateHeaderView(mLatestData);
                        mView.updateNewsList(mListNewsData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.closeRefreshUI();
                        mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.closeRefreshUI();
                    }
                });
    }

    private void LoadBeforeData() {
        mDate = DateUtil.getBeforeDate(mDate);
        String date = DateUtil.formatDate(mDate, "yyyyMMdd");
        Observable<BeforeStoryBean> observable = mApiService.beforeNews(date);
        mBeforeSubscribe = observable.subscribeOn(Schedulers.io())
                .filter(new Func1<BeforeStoryBean, Boolean>() {
                    @Override
                    public Boolean call(BeforeStoryBean beforeStoryBean) {
                        return beforeStoryBean != null;
                    }
                }).doOnNext(new Action1<BeforeStoryBean>() {
                    @Override
                    public void call(BeforeStoryBean beforeStoryBean) {
                       initReadState(beforeStoryBean.getListStories());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BeforeStoryBean>() {
                    @Override
                    public void call(BeforeStoryBean beforeStoryBean) {
                        mListNewsData.add(getDateItem(mDate));
                        mListNewsData.addAll(beforeStoryBean.getListStories());
                        mView.updateNewsList(mListNewsData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }

    protected abstract Observable<M> getLatestObservable(ApiService apiService, P params);

    private void initReadState(List<ListStoryBean> beans) {
        RealmManager realmManager = new RealmManager();
        for(ListStoryBean bean : beans) {
            bean.setRead(realmManager.queryNewsReadState(bean.getId()));
        }
        realmManager.close();
    }

    @Override
    public void setReadState(ListStoryBean bean) {
        RealmManager realmManager = new RealmManager();
        realmManager.insertNewsReadState(bean.getId());
        realmManager.close();
    }

    private ListStoryBean getDateItem(Date date) {
        ListStoryBean listStoryBean = new ListStoryBean();
        listStoryBean.setDate(true);
        String dateString = DateUtil.formatDate(date);
        listStoryBean.setDateString(dateString);
        return listStoryBean;
    }

    private ListStoryBean getTitleItem() {
        ListStoryBean listStoryBean = new ListStoryBean();
        listStoryBean.setTitle(true);
        return listStoryBean;
    }
}
