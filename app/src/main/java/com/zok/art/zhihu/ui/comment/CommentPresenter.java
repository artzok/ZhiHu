package com.zok.art.zhihu.ui.comment;

import android.content.Intent;
import android.widget.ListView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.CommentBean;
import com.zok.art.zhihu.bean.CommentsBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
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
    private List<CommentBean> mComments;

    private boolean hasLoad;

    public CommentPresenter(Intent intent) {
        mNewsId = intent.getLongExtra(CommentActivity.EXTRA_NEWS_ID, 0);
        mNewsExtraInfo = intent.getParcelableExtra(CommentActivity.EXTRA_COMMENT_INFO);
        mApiService = ApiManager.getApiService();
    }

    @Override
    public void attachView(CommentContract.View view) {
        mView = view;
        mView.updateTitle(String.format(Locale.CHINA, "%d条点评", mNewsExtraInfo.getComments()));
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void start() {
        loadLongComment();
    }

    @Override
    public void loadLongComment() {
        if (mNewsExtraInfo.getLong_comments() == 0) {
            concat(LONG_TYPE, null);
            return;
        }
        Observable<CommentsBean> longComment = mApiService.getLongComment(mNewsId);
        longComment.compose(new CommentTransformer()).subscribe(new Action1<List<CommentBean>>() {
            @Override
            public void call(List<CommentBean> commentBeen) {
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
        if (hasLoad) {
            hasLoad = false;
            mComments = mComments.subList(0, mNewsExtraInfo.getLong_comments() + 2);
            concat(-1, mComments);
            return;
        }

        if (mNewsExtraInfo.getShort_comments() == 0) {
            concat(SHORT_TYPE, null);
            return;
        }
        Observable<CommentsBean> longComment = mApiService.getShortComment(mNewsId);
        longComment.compose(new CommentTransformer()).subscribe(new Action1<List<CommentBean>>() {
            @Override
            public void call(List<CommentBean> commentBeen) {
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

    private void concat(int commentType, List<CommentBean> comments) {
        if (commentType == LONG_TYPE) {
            if (comments != null) {
                mComments = comments;
            } else {
                mComments = new ArrayList<>();
            }
            mComments.add(0, new CommentBean());
            mComments.add(new CommentBean());
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

    private class CommentTransformer implements Observable.Transformer<CommentsBean, List<CommentBean>> {
        @Override
        public Observable<List<CommentBean>> call(Observable<CommentsBean> origin) {
            return origin.subscribeOn(Schedulers.io())
                    .filter(new Func1<CommentsBean, Boolean>() {
                        @Override
                        public Boolean call(CommentsBean commentsBean) {
                            return commentsBean != null;
                        }
                    }).flatMap(new Func1<CommentsBean, Observable<List<CommentBean>>>() {
                        @Override
                        public Observable<List<CommentBean>> call(CommentsBean commentsBean) {
                            return Observable.just(commentsBean.getComments());
                        }
                    }).filter(new Func1<List<CommentBean>, Boolean>() {
                        @Override
                        public Boolean call(List<CommentBean> commentBeen) {
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
