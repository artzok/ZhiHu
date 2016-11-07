package com.zok.art.zhihu.di.component;

import com.zok.art.zhihu.di.module.FragmentModule;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.section.SectionFragment;
import com.zok.art.zhihu.ui.sections.SectionsFragment;
import com.zok.art.zhihu.ui.theme.ThemeFragment;
import com.zok.art.zhihu.ui.themes.ThemesFragment;

import dagger.Component;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
@Component(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(HomeFragment fragment);
    void inject(SectionsFragment fragment);
    void inject(SectionFragment fragment);
    void inject(ThemeFragment fragment);
    void inject(ThemesFragment fragment);
}
