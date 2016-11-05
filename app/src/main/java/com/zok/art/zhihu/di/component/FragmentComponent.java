package com.zok.art.zhihu.di.component;

import com.zok.art.zhihu.di.module.FragmentModule;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.themes.ThemeFragment;

import dagger.Component;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
@Component(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(HomeFragment splashActivity);
    void inject(ThemeFragment splashActivity);
}
