package com.zok.art.zhihu.di.module;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.home.HomePresenter;
import com.zok.art.zhihu.ui.section.SectionFragment;
import com.zok.art.zhihu.ui.section.SectionPresenter;
import com.zok.art.zhihu.ui.sections.SectionsFragment;
import com.zok.art.zhihu.ui.sections.SectionsPresenter;
import com.zok.art.zhihu.ui.theme.ThemeFragment;
import com.zok.art.zhihu.ui.theme.ThemePresenter;
import com.zok.art.zhihu.ui.themes.ThemesFragment;
import com.zok.art.zhihu.ui.themes.ThemesPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
@Module
public class FragmentModule {
    private Fragment mFragment;
    private Bundle initParams;

    public FragmentModule(Fragment fragment, Bundle initParams) {
        this.mFragment = fragment;
        this.initParams = initParams;
    }

    @Provides
    public HomePresenter provideHomePagePresenter() {
        HomePresenter presenter = new HomePresenter();
        presenter.setParams(initParams);
        presenter.attachView((HomeFragment) mFragment);
        return presenter;
    }

    @Provides
    public ThemePresenter provideThemePagePresenter() {
        ThemePresenter presenter = new ThemePresenter();
        presenter.setParams(initParams);
        presenter.attachView((ThemeFragment) mFragment);
        return presenter;
    }

    @Provides
    public SectionPresenter provideSectionPagePresenter() {
        SectionPresenter presenter = new SectionPresenter();
        presenter.setParams(initParams);
        presenter.attachView((SectionFragment) mFragment);
        return presenter;
    }

    @Provides
    public SectionsPresenter provideSectionsPresenter() {
        SectionsPresenter presenter = new SectionsPresenter();
        presenter.attachView((SectionsFragment) mFragment);
        return presenter;
    }

    @Provides
    public ThemesPresenter provideThemesPresenter() {
        ThemesPresenter presenter = new ThemesPresenter();
        presenter.attachView((ThemesFragment) mFragment);
        return presenter;
    }
}
