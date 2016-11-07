package com.zok.art.zhihu.ui.refresh;

import android.os.Bundle;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.base.BaseFragmentPresenter;
import com.zok.art.zhihu.bean.BasicStoryBean;
import com.zok.art.zhihu.bean.StoriesBeforeBean;
import com.zok.art.zhihu.bean.StoryListItemBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.db.RealmManager;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.CastUtil;
import com.zok.art.zhihu.utils.DateUtil;
import com.zok.art.zhihu.utils.RxJavaUtils;

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
        extends BaseFragmentPresenter<V> implements RefreshContract.Presenter<M, V> {
    protected P mInitParams;

    // latest data and list
    protected M mLatestData;
    private List<StoryListItemBean> mListNewsData;

    // URL API Service
    private ApiService mApiService;

    private Subscription mLatestSubscribe;
    private Subscription mBeforeSubscribe;

    private Date mDate;

    @Override
    public void setParams(Bundle initParams) {
        if (initParams != null)
            mInitParams = CastUtil.cast(initParams.getParcelable(Constants.EXTRA_INIT_PARAMS));
        mApiService = ApiManager.getApiService();
    }

    @Override
    public void start() {
        mView.updateTitle(getTitle());
        updateLatest();
    }

    @Override
    public void stopUpdate() {
        mView.closeRefreshUI();
        RxJavaUtils.releaseSubscribe(mLatestSubscribe);
        RxJavaUtils.releaseSubscribe(mBeforeSubscribe);
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
                        mView.updateBanner(mLatestData);
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
        if(mDate == null) mDate = new Date();
        mDate = DateUtil.getBeforeDate(mDate);
        Observable<StoriesBeforeBean> observable = getBeforeObservable(mApiService, mInitParams, mDate);
        mBeforeSubscribe = observable.subscribeOn(Schedulers.io())
                .filter(new Func1<StoriesBeforeBean, Boolean>() {
                    @Override
                    public Boolean call(StoriesBeforeBean storiesBeforeBean) {
                        return storiesBeforeBean != null;
                    }
                }).doOnNext(new Action1<StoriesBeforeBean>() {
                    @Override
                    public void call(StoriesBeforeBean storiesBeforeBean) {
                        initReadState(storiesBeforeBean.getListStories());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StoriesBeforeBean>() {
                    @Override
                    public void call(StoriesBeforeBean storiesBeforeBean) {
                        mListNewsData.add(getDateItem(mDate));
                        mListNewsData.addAll(storiesBeforeBean.getListStories());
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

    protected Observable<StoriesBeforeBean> getBeforeObservable(ApiService apiService, P params, Date date) {
        return mApiService.getBeforeNews(DateUtil.formatDate(date, "yyyyMMdd"));
    }


    private void initReadState(List<? extends BasicStoryBean> beans) {
        RealmManager realmManager = RealmManager.getAsyncInstance();
        for (BasicStoryBean bean : beans) {
            bean.setRead(realmManager.isRead(bean.getId()));
        }
        realmManager.close();
    }

    @Override
    public void setReadState(StoryListItemBean bean) {
        RealmManager realmManager = RealmManager.getInstance();
        realmManager.setRead(bean);
    }

    private StoryListItemBean getDateItem(Date date) {
        StoryListItemBean storyListItemBean = new StoryListItemBean();
        storyListItemBean.setDate(true);
        String dateString = DateUtil.formatDate(date);
        storyListItemBean.setDateString(dateString);
        return storyListItemBean;
    }

    private StoryListItemBean getTitleItem() {
        StoryListItemBean storyListItemBean = new StoryListItemBean();
        storyListItemBean.setTitle(true);
        return storyListItemBean;
    }
}
