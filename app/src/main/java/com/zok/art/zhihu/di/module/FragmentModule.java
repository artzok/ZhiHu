package com.zok.art.zhihu.di.module;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zok.art.zhihu.ui.homepage.HomePageContract;
import com.zok.art.zhihu.ui.homepage.HomePagePresenter;
import com.zok.art.zhihu.ui.themes.ThemePageContract;
import com.zok.art.zhihu.ui.themes.ThemePagePresenter;

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
    public HomePageContract.Presenter provideHomePagePresenter() {
        HomePagePresenter presenter = new HomePagePresenter(initParams);
        presenter.attachView((HomePageContract.View) mFragment);
        return presenter;
    }

    @Provides
    public ThemePageContract.Presenter provideThemePagePresenter() {
        ThemePagePresenter presenter = new ThemePagePresenter(initParams);
        presenter.attachView((ThemePageContract.View) mFragment);
        return presenter;
    }
}
