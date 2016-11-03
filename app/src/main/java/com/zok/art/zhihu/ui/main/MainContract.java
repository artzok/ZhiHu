package com.zok.art.zhihu.ui.main;

import android.support.v4.app.Fragment;

import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;
import com.zok.art.zhihu.bean.ThemeBean;
import com.zok.art.zhihu.bean.TopStoryBean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface MainContract {
    interface View extends BaseView {
        void updateActionBarTitle(String title);
        void updateThemeList(List<ThemeBean> themes);
        void replaceFragment(Fragment instance);

    }

    interface Presenter extends BasePresenter<View> {
        void changedTheme(int position);
        void changedHome();
    }
}
