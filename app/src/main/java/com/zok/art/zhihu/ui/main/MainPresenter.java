package com.zok.art.zhihu.ui.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.ThemeBean;
import com.zok.art.zhihu.bean.ThemesBean;
import com.zok.art.zhihu.ui.homepage.HomePageFragment;
import com.zok.art.zhihu.ui.themes.ThemePageFragment;
import com.zok.art.zhihu.utils.AppUtil;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.zok.art.zhihu.ui.main.MainContract.Presenter;
import static com.zok.art.zhihu.ui.main.MainContract.View;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class MainPresenter implements Presenter {

    private View mMainView;
    private ApiService mService;
    private List<ThemeBean> mThemeBeans;
    private Fragment mHomePageFragment;
    private SparseArray<Fragment> mThemeFragments;

    public MainPresenter() {
        this(null);
    }

    public MainPresenter(Intent intent) {
        mService = ApiManager.getApiService();
        mHomePageFragment = HomePageFragment.newInstance();
        mThemeFragments = new SparseArray<>();

    }

    @Override
    public void attachView(View view) {
        this.mMainView = view;
    }

    @Override
    public void detachView() {
        mMainView = null;
    }

    @Override
    public void start() {
        // 初始化导航列表
        loadThemes();
//        // go to home page
//        changedHome();
    }

    @Override
    public void changedTheme(int position) {
        // update theme title
        ThemeBean bean = mThemeBeans.get(position);
        mMainView.updateActionBarTitle(bean.getName());
        // fragment trans
        mMainView.replaceFragment(getThemeFragment(position));
    }

    @Override
    public void changedHome() {
        // update title
        mMainView.updateActionBarTitle(AppUtil.getString(R.string.menu_home_tip));
        // fragment trans
        mMainView.replaceFragment(mHomePageFragment);
    }

    private Fragment getThemeFragment(int i) {
        Fragment fragment = mThemeFragments.get(i);
        if (fragment == null) {
            fragment = ThemePageFragment.newInstance(mThemeBeans.get(i));
            mThemeFragments.put(i, fragment);
        }
        return fragment;
    }

    private void loadThemes() {
        mService.newestThemes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThemesBean>() {
                    @Override
                    public void call(ThemesBean themesBean) {
                        mThemeBeans = themesBean.getOthers();
                        if (mMainView != null)
                            mMainView.updateThemeList(mThemeBeans);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mMainView != null)
                            mMainView.showError("Load themes error!", throwable);
                    }
                });
    }
}
