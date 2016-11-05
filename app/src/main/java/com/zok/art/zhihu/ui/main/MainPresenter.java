package com.zok.art.zhihu.ui.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.SectionListBean;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.bean.ThemeListBean;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.themes.ThemeFragment;
import com.zok.art.zhihu.utils.AppUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zok.art.zhihu.ui.main.MainContract.Presenter;
import static com.zok.art.zhihu.ui.main.MainContract.View;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class MainPresenter implements Presenter {

    private View mView;
    private ApiService mService;
    private Fragment mHomePageFragment;
    private List<ThemeItemBean> mThemeItemBeen;
    private SparseArray<Fragment> mThemeFragments;

    public MainPresenter() {
        this(null);
    }

    public MainPresenter(Intent intent) {
        mService = ApiManager.getApiService();
        mHomePageFragment = HomeFragment.newInstance();
        mThemeFragments = new SparseArray<>();

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
        loadThemes();
    }

    @Override
    public void switchTheme(int position) {
        // update theme title
        ThemeItemBean bean = mThemeItemBeen.get(position);
        mView.updateTitle(bean.getName());
        // fragment trans
        mView.replaceFragment(getThemeFragment(position));
    }

    @Override
    public void switchHome() {
        // update title
        mView.updateTitle(AppUtil.getString(R.string.menu_home_tip));
        // fragment trans
        mView.replaceFragment(mHomePageFragment);
    }

    private Fragment getThemeFragment(int i) {
        Fragment fragment = mThemeFragments.get(i);
        if (fragment == null) {
            fragment = ThemeFragment.newInstance(mThemeItemBeen.get(i));
            mThemeFragments.put(i, fragment);
        }
        return fragment;
    }

    private void loadThemes() {
        mService.newestThemes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThemeListBean>() {
                    @Override
                    public void call(ThemeListBean themeListBean) {
                        mThemeItemBeen = themeListBean.getOthers();
                        if (mView != null)
                            mView.updateThemeList(mThemeItemBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mView != null)
                            mView.showError("Load themes error!", throwable);
                    }
                });
    }
}
