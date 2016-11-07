package com.zok.art.zhihu.ui.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.section.SectionFragment;
import com.zok.art.zhihu.ui.sections.SectionsFragment;
import com.zok.art.zhihu.ui.theme.ThemeFragment;
import com.zok.art.zhihu.ui.themes.ThemesFragment;

import static com.zok.art.zhihu.ui.main.MainContract.Presenter;
import static com.zok.art.zhihu.ui.main.MainContract.View;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class MainPresenter implements Presenter {
    private View mView;

    // 四大Fragment
    private BaseFragment mHomePageFragment;
    private BaseFragment mSectionsFragment;
    private BaseFragment mThemeFragment;
    private BaseFragment mSectionFragment;
    private BaseFragment mThemesFragment;

    public MainPresenter() {
        mHomePageFragment = HomeFragment.newInstance();
        mSectionsFragment = SectionsFragment.newInstance();
        mThemesFragment = ThemesFragment.newInstance();
    }

    @Override
    public void attachView(View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void start() {

    }

    @Override
    public void switchHome() {
        // fragment trans
        mView.replaceFragment(mHomePageFragment);
    }

    @Override
    public void switchThemes() {
        mView.replaceFragment(mThemesFragment);
    }

    @Override
    public void switchSections() {
        mView.replaceFragment(mSectionsFragment);
    }

    @Override
    public void switchTheme(ThemeItemBean bean) {
        if(mThemeFragment == null) {
            mThemeFragment = ThemeFragment.newInstance(bean);
        } else {
            Bundle bundle = getBundle(bean);
            mThemeFragment.getPresenter().setParams(bundle);
        }
        mView.replaceFragment(mThemeFragment);
    }

    @Override
    public void switchSection(SectionBean bean) {
        if(mSectionFragment == null) {
            mSectionFragment = SectionFragment.newInstance(bean);
        } else {
            mSectionFragment.getPresenter().setParams(getBundle(bean));
        }
        mView.replaceFragment(mSectionFragment);
    }

    @NonNull
    private Bundle getBundle(Parcelable bean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_INIT_PARAMS, bean);
        return bundle;
    }
}
