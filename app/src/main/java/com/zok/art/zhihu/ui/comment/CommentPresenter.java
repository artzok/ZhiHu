package com.zok.art.zhihu.ui.comment;

import android.content.Intent;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.CommentItemBean;
import com.zok.art.zhihu.bean.CommentListBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.RxJavaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class CommentPresenter implements CommentContract.Presenter {
    private static final int LONG_TYPE = 1;
    private static final int SHORT_TYPE = 2;

    // 新闻ID
    private long mNewsId;
    // 评论等额外信息
    private NewsExtraBean mNewsExtraInfo;

    // View
    private CommentContract.View mView;

    // API
    private final ApiService mApiService;
    private List<CommentItemBean> mComments;

    // 评论是否已经加载
    private boolean hasLoad;

    // 相关订阅者
    private Subscription mLongSubscribe;
    private Subscription mShortSubscribe;

    public CommentPresenter(Intent intent) {
        mNewsId = intent.getLongExtra(CommentActivity.EXTRA_NEWS_ID, 0);
        mNewsExtraInfo = intent.getParcelableExtra(CommentActivity.EXTRA_COMMENT_INFO);
        mApiService = ApiManager.getApiService();
    }

    @Override
    public void attachView(CommentContract.View view) {
        mView = view;
        int comments = mNewsExtraInfo.getComments();
        mView.updateTitle(String.format(Locale.CHINA, "%d条点评", comments));
    }

    @Override
    public void detachView() {
        RxJavaUtils.releaseSubscribe(mLongSubscribe);
        RxJavaUtils.releaseSubscribe(mShortSubscribe);
        mView = null;
    }

    @Override
    public void start() {
        loadLongComment();
    }

    @Override
    public void loadLongComment() {
        // 避免没有意义的请求
        if (mNewsExtraInfo.getLong_comments() == 0) {
            concat(LONG_TYPE, null);
            return;
        }

        // 请求长评
        Observable<CommentListBean> longComment = mApiService.getLongComment(mNewsId);
        mLongSubscribe = longComment.compose(new CommentTransformer())
                .subscribe(new Action1<List<CommentItemBean>>() {
                    @Override
                    public void call(List<CommentItemBean> commentBeen) {
                        concat(LONG_TYPE, commentBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }

    @Override
    public void loadOrDeleteShortComment() {
        // 折叠效果
        if (hasLoad) {
            hasLoad = false;
            mComments = mComments.subList(0, mNewsExtraInfo.getLong_comments() + 2);
            concat(-1, mComments);// just update
            return;
        }

        // 避免没有意义的请求
        if (mNewsExtraInfo.getShort_comments() == 0) {
//            concat(SHORT_TYPE, null);
            return;
        }

        // 请求短评
        Observable<CommentListBean> longComment = mApiService.getShortComment(mNewsId);
        mShortSubscribe = longComment.compose(new CommentTransformer())
                .subscribe(new Action1<List<CommentItemBean>>() {
                    @Override
                    public void call(List<CommentItemBean> commentBeen) {
                        concat(SHORT_TYPE, commentBeen);
                        hasLoad = true;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }

    private void concat(int commentType, List<CommentItemBean> comments) {
        if (commentType == LONG_TYPE) {
            if (comments != null) {
                mComments = comments;
            } else {
                mComments = new ArrayList<>();
            }
            mComments.add(0, new CommentItemBean());
            mComments.add(new CommentItemBean());
        } else if (commentType == SHORT_TYPE) {
            if (comments != null)
                mComments.addAll(comments);
        }

        // 更新评论数量
        mView.updateLongComment(mNewsExtraInfo.getLong_comments(),
                mNewsExtraInfo.getShort_comments(), mComments);

        // 关闭进度条
        mView.closeProgressBar();
    }

    private class CommentTransformer implements Observable.Transformer<CommentListBean, List<CommentItemBean>> {
        @Override
        public Observable<List<CommentItemBean>> call(Observable<CommentListBean> origin) {
            return origin.subscribeOn(Schedulers.io())
                    .filter(new Func1<CommentListBean, Boolean>() {
                        @Override
                        public Boolean call(CommentListBean commentListBean) {
                            return commentListBean != null;
                        }
                    }).flatMap(new Func1<CommentListBean, Observable<List<CommentItemBean>>>() {
                        @Override
                        public Observable<List<CommentItemBean>> call(CommentListBean commentListBean) {
                            return Observable.just(commentListBean.getComments());
                        }
                    }).filter(new Func1<List<CommentItemBean>, Boolean>() {
                        @Override
                        public Boolean call(List<CommentItemBean> commentBeen) {
                            return commentBeen != null && commentBeen.size() > 0;
                        }
                    }).doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            mView.showProgressBar();
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }
}
