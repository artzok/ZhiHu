package com.zok.art.zhihu.di.component;

import com.zok.art.zhihu.di.module.FragmentModule;
import com.zok.art.zhihu.ui.homepage.HomePageFragment;
import com.zok.art.zhihu.ui.themes.ThemePageFragment;

import dagger.Component;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
@Component(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(HomePageFragment splashActivity);
    void inject(ThemePageFragment splashActivity);
}
