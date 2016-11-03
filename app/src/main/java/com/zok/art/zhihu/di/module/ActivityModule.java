package com.zok.art.zhihu.di.module;

import android.app.Activity;
import android.content.Intent;

import com.zok.art.zhihu.ui.comment.CommentContract;
import com.zok.art.zhihu.ui.comment.CommentPresenter;
import com.zok.art.zhihu.ui.main.MainContract;
import com.zok.art.zhihu.ui.main.MainPresenter;
import com.zok.art.zhihu.ui.detail.NewsDetailContract;
import com.zok.art.zhihu.ui.detail.NewsDetailPresenter;
import com.zok.art.zhihu.ui.splash.SplashContract;
import com.zok.art.zhihu.ui.splash.SplashPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
@Module
public class ActivityModule {
    private Activity mActivity;
    private Intent mIntent;

    public ActivityModule(Activity activity, Intent intent) {
        this.mActivity = activity;
        mIntent = intent;
    }

    @Provides
    public SplashContract.Presenter provideSplashPresenter() {
        SplashPresenter presenter = new SplashPresenter();
        presenter.attachView((SplashContract.View) mActivity);
        return presenter;
    }

    @Provides
    public MainContract.Presenter provideMainPresenter() {
        MainPresenter presenter = new MainPresenter();
        presenter.attachView((MainContract.View) mActivity);
        return presenter;
    }

    @Provides
    public NewsDetailContract.Presenter provideNewsDetailPresenter() {
        NewsDetailPresenter presenter = new NewsDetailPresenter(mIntent);
        presenter.attachView((NewsDetailContract.View) mActivity);
        return presenter;
    }

    @Provides
    public CommentContract.Presenter provideCommentPresenter() {
        CommentPresenter presenter = new CommentPresenter(mIntent);
        presenter.attachView((CommentContract.View) mActivity);
        return presenter;
    }
}
