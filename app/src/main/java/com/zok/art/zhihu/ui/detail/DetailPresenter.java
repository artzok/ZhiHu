package com.zok.art.zhihu.ui.detail;

import android.content.Intent;
import android.text.TextUtils;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.BasicStoryBean;
import com.zok.art.zhihu.bean.NewsDetailBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.bean.StoryListItemBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.db.RealmManager;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.HtmlUtil;

import java.io.IOException;

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
public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View mView;
    private Subscription mSubscription;
    private NewsExtraBean mNewsExtra;
    private StoryListItemBean mStoryBean;
    private RealmManager mInstance;

    public DetailPresenter(Intent intent) {
        mStoryBean = intent.getParcelableExtra(Constants.EXTRA_INIT_PARAMS);
        mInstance = RealmManager.getInstance();
    }

    @Override
    public void attachView(DetailContract.View view) {
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
        Observable<NewsDetailBean> observable = apiService.newsDetails(mStoryBean.getId());
        mSubscription = observable.subscribeOn(Schedulers.io())
                .filter(new Func1<NewsDetailBean, Boolean>() {
                    @Override
                    public Boolean call(NewsDetailBean newsDetailBean) {
                        return newsDetailBean != null;
                    }
                }).doOnNext(new Action1<NewsDetailBean>() {
                    @Override
                    public void call(final NewsDetailBean newsDetailBean) {
                        // 部分新闻为空，需要重试
                        if (TextUtils.isEmpty(newsDetailBean.getHtmlBody())) {
                            apiService.getData(newsDetailBean.getShareUrl())
                                    .subscribe(new Action1<ResponseBody>() {
                                        @Override
                                        public void call(ResponseBody responseBody) {
                                            try {
                                                newsDetailBean.setHtmlBody(responseBody.string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            mView.showError(AppUtil.getString(R.string.load_failed), throwable);
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
                        mView.updateHeaderImage(bean.getLagerImageUrl());
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
        final Observable<NewsExtraBean> newsExtra = apiService.getNewsExtra(mStoryBean.getId());
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
    public BasicStoryBean getStoryBean() {
        return mStoryBean;
    }

    @Override
    public NewsExtraBean getNewsExtraInfo() {
        return mNewsExtra;
    }

    @Override
    public boolean isCollected() {
        return mInstance.isCollected(mStoryBean.getId());
    }

    @Override
    public boolean isPraised() {
        return mInstance.isPraised(mStoryBean.getId());
    }

    @Override
    public void setCollected(boolean collected) {
        mInstance.setCollected(mStoryBean, collected);
    }

    @Override
    public void setPraised(boolean praised) {
        mInstance.setPraised(mStoryBean, praised);
    }


}
