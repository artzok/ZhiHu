package com.zok.art.zhihu.di.module;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zok.art.zhihu.ui.home.HomeContract;
import com.zok.art.zhihu.ui.home.HomePresenter;
import com.zok.art.zhihu.ui.themes.ThemeContract;
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
    public HomeContract.Presenter provideHomePagePresenter() {
        HomePresenter presenter = new HomePresenter(initParams);
        presenter.attachView((HomeContract.View) mFragment);
        return presenter;
    }

    @Provides
    public ThemeContract.Presenter provideThemePagePresenter() {
        ThemePresenter presenter = new ThemePresenter(initParams);
        presenter.attachView((ThemeContract.View) mFragment);
        return presenter;
    }
}
