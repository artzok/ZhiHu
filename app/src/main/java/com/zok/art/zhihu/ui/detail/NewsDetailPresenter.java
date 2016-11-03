package com.zok.art.zhihu.ui.detail;

import android.content.Intent;
import android.text.TextUtils;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.NewsDetailBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.HtmlUtil;

import java.io.IOException;

import javax.annotation.CheckForNull;

import okhttp3.ResponseBody;
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
public class NewsDetailPresenter implements NewsDetailContract.Presenter {
    private NewsDetailContract.View mView;
    private long mNewsId;
    private Subscription mSubscription;
    private NewsExtraBean mNewsExtra;

    public NewsDetailPresenter(Intent intent) {
        mNewsId = intent.getLongExtra(Constants.EXTRA_INIT_PARAMS, 0);
    }

    @Override
    public void attachView(NewsDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mView = null;
    }

    @Override
    public void start() {
        loadContent(ApiManager.getApiService());
        loadExtra(ApiManager.getApiService());
    }

    private void loadContent(final ApiService apiService) {
        Observable<NewsDetailBean> observable = apiService.newsDetails(mNewsId);
        mSubscription = observable.subscribeOn(Schedulers.io())
                .filter(new Func1<NewsDetailBean, Boolean>() {
                    @Override
                    public Boolean call(NewsDetailBean newsDetailBean) {
                        return newsDetailBean != null;
                    }
                }).doOnNext(new Action1<NewsDetailBean>() {
                    @Override
                    public void call(final NewsDetailBean newsDetailBean) {
                        if (TextUtils.isEmpty(newsDetailBean.getHtmlBody())) {
                            apiService.getData(newsDetailBean.getShareUrl())
                                    .subscribe(
                                            new Action1<ResponseBody>() {
                                                @Override
                                                public void call(ResponseBody responseBody) {
                                                    try {
                                                        newsDetailBean.setHtmlBody(responseBody.string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                        }
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showProgress();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsDetailBean>() {
                    @Override
                    public void call(NewsDetailBean bean) {
                        String html = HtmlUtil.createHtmlData(bean.getHtmlBody(),
                                bean.getCss(), bean.getJavaScripts());
                        mView.updateWebView(html);
                        mView.updateTitle(bean.getTitle());
                        mView.updateHeadImage(bean.getLagerImageUrl());
                        mView.updateCopyRight(bean.getImageAuthor());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }

    private void loadExtra(ApiService apiService) {
        final Observable<NewsExtraBean> newsExtra = apiService.getNewsExtra(mNewsId);
        newsExtra.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showProgress();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsExtraBean>() {
                    @Override
                    public void call(NewsExtraBean newsExtraBean) {
                        mNewsExtra = newsExtraBean;
                        if (mView != null)
                            mView.updateExtra(mNewsExtra);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }

    @Override
    public long getNewsId() {
        return mNewsId;
    }

    @Override
    public NewsExtraBean getNewsExtraInfo() {
        return mNewsExtra;
    }

}
