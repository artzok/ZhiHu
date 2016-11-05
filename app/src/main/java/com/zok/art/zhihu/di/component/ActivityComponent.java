package com.zok.art.zhihu.di.component;

import com.zok.art.zhihu.di.module.ActivityModule;
import com.zok.art.zhihu.ui.comment.CommentActivity;
import com.zok.art.zhihu.ui.detail.DetailActivity;
import com.zok.art.zhihu.ui.main.MainActivity;
import com.zok.art.zhihu.ui.splash.SplashActivity;

import dagger.Component;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(SplashActivity splashActivity);
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
    void inject(CommentActivity commentActivity);
}
