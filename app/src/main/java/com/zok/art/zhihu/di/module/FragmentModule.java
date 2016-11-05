package com.zok.art.zhihu.di.module;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.home.HomePresenter;
import com.zok.art.zhihu.ui.section.SectionFragment;
import com.zok.art.zhihu.ui.section.SectionPresenter;
import com.zok.art.zhihu.ui.sections.SectionsContract;
import com.zok.art.zhihu.ui.sections.SectionsPresenter;
import com.zok.art.zhihu.ui.themes.ThemeFragment;
import com.zok.art.zhihu.ui.themes.ThemePresenter;

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
        HomePresenter presenter = new HomePresenter(initParams);
        presenter.attachView((HomeFragment) mFragment);
        return presenter;
    }

    @Provides
    public ThemePresenter provideThemePagePresenter() {
        ThemePresenter presenter = new ThemePresenter(initParams);
        presenter.attachView((ThemeFragment) mFragment);
        return presenter;
    }

    @Provides
    public SectionPresenter provideSectionPagePresenter() {
        SectionPresenter presenter = new SectionPresenter(initParams);
        presenter.attachView((SectionFragment) mFragment);
        return presenter;
    }

    @Provides
    public SectionsContract.Presenter provideSectionsPresenter() {
        SectionsPresenter presenter = new SectionsPresenter();
        presenter.attachView((SectionsContract.View) mFragment);
        return presenter;
    }
}
