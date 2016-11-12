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
    private static final int LONG_TYPE_INIT = 1;
    private static final int SHORT_TYPE_INIT = 2;
    private static final int LONG_TYPE_MORE = 3;
    private static final int SHORT_TYPE_MORE = 4;

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
    private Subscription mLongSubscribeMore;
    private Subscription mShortSubscribeMore;
    private int longCount;      // 长评数量
    private int shortCount;     // 短评数量
    private Action1<Throwable> mOnError;// 错误处理对象

    public CommentPresenter(Intent intent) {
        mNewsId = intent.getLongExtra(CommentActivity.EXTRA_NEWS_ID, 0);
        mNewsExtraInfo = intent.getParcelableExtra(CommentActivity.EXTRA_COMMENT_INFO);
        mApiService = ApiManager.getApiService();
    }

    @Override
    public void attachView(CommentContract.View view) {
        mView = view;
        mOnError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                mView.closeProgressBar();
            }
        };
    }

    @Override
    public void detachView() {
        RxJavaUtils.releaseSubscribe(mLongSubscribe);
        RxJavaUtils.releaseSubscribe(mLongSubscribeMore);

        RxJavaUtils.releaseSubscribe(mShortSubscribe);
        RxJavaUtils.releaseSubscribe(mShortSubscribeMore);
        mView = null;
    }

    @Override
    public void start() {
        int comments = mNewsExtraInfo.getComments();
        mView.updateTitle(String.format(Locale.CHINA, "%d条点评", comments));
        loadLongComment();
    }

    @Override
    public void loadLongComment() {
        // 避免没有意义的请求
        if (mNewsExtraInfo.getLong_comments() == 0) {
            concat(LONG_TYPE_INIT, null);
            return;
        }
        // 请求长评
        Observable<CommentListBean> longComment = mApiService.getLongComment(mNewsId);
        mLongSubscribe = longComment.compose(new CommentTransformer())
                .subscribe(new Action1<List<CommentItemBean>>() {
                    @Override
                    public void call(List<CommentItemBean> commentBeen) {
                        concat(LONG_TYPE_INIT, commentBeen);
                    }
                }, mOnError);
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
            return;
        }

        // 请求短评
        Observable<CommentListBean> longComment = mApiService.getShortComment(mNewsId);
        mShortSubscribe = longComment.compose(new CommentTransformer())
                .subscribe(new Action1<List<CommentItemBean>>() {
                    @Override
                    public void call(List<CommentItemBean> commentBeen) {
                        concat(SHORT_TYPE_INIT, commentBeen);
                        hasLoad = true;
                    }
                }, mOnError);
    }

    @Override
    public void loadMore(int position) {
        if (longCount > 0 && longCount < mNewsExtraInfo.getLong_comments() && position >= longCount) {
            if(mLongSubscribeMore != null && !mLongSubscribeMore.isUnsubscribed()) return;
            // load more long comment
            long lastId = mComments.get(longCount).getTime();
            Observable<CommentListBean> longCommentMore = mApiService.getLongCommentMore(mNewsId, lastId);
            mLongSubscribeMore = longCommentMore.compose(new CommentTransformer())
                    .subscribe(new Action1<List<CommentItemBean>>() {
                        @Override
                        public void call(List<CommentItemBean> commentBeen) {
                            concat(LONG_TYPE_MORE, commentBeen);
                        }
                    }, mOnError);
        } else if (shortCount > 0 && shortCount < mNewsExtraInfo.getShort_comments()
                && position >= longCount + shortCount + 1) {
            if(mShortSubscribeMore != null && !mShortSubscribeMore.isUnsubscribed()) return;
            // load more short comment
            long lastId = mComments.get(mComments.size() - 1).getTime();
            Observable<CommentListBean> shortCommentMore = mApiService.getShortCommentMore(mNewsId, lastId);
            mShortSubscribeMore = shortCommentMore.compose(new CommentTransformer())
                    .subscribe(new Action1<List<CommentItemBean>>() {
                        @Override
                        public void call(List<CommentItemBean> commentBeen) {
                            concat(SHORT_TYPE_MORE, commentBeen);
                        }
                    }, mOnError);
        }
    }

    private void concat(int commentType, List<CommentItemBean> comments) {
        if (commentType == LONG_TYPE_INIT) {
            if (comments != null) {
                mComments = comments;
                longCount = comments.size();
            } else {
                mComments = new ArrayList<>();
            }
            mComments.add(0, new CommentItemBean());
            mComments.add(new CommentItemBean());
        } else if (commentType == SHORT_TYPE_INIT) {
            if (comments != null) {
                mComments.addAll(comments);
                shortCount = comments.size();
            }
        } else if (commentType == LONG_TYPE_MORE) {
            if (comments != null) {
                mComments.addAll(longCount + 1, comments);
                longCount += comments.size();
            }
        } else if (commentType == SHORT_TYPE_MORE) {
            if (comments != null) {
                mComments.addAll(comments);
                shortCount += comments.size();
            }
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
