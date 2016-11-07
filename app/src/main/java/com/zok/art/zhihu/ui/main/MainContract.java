package com.zok.art.zhihu.ui.main;

import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.ThemeItemBean;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface MainContract {
    interface View extends BaseView {
        /**
         * 更新页面标题
         *
         * @param title 标题
         */
        void updateTitle(String title);

        /**
         * 替换Fragment实例
         *
         * @param instance fragment实例
         */
        void replaceFragment(BaseFragment instance);

    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 切换到指定主题
         *
         * @param bean 指定位置
         */
        void switchTheme(ThemeItemBean bean);

        /**
         * 切换到Home页面
         */
        void switchHome();

        void switchSections();

        void switchThemes();

        void switchSection(SectionBean bean);
    }
}
