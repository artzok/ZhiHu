package com.zok.art.zhihu.ui.main;

import android.support.v4.app.Fragment;

import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.ThemeItemBean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface MainContract {
    interface View extends BaseView {
        /**
         * 更新页面标题
         * @param title 标题
         */
        void updateTitle(String title);

        /**
         * 更新主题列表
         * @param themes 主题数据集
         */
        void updateThemeList(List<ThemeItemBean> themes);

        /**
         * 替换Fragment实例
         * @param instance fragment实例
         */
        void replaceFragment(Fragment instance);

    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 切换到指定主题
         * @param position 指定位置
         */
        void switchTheme(int position);

        /**
         * 切换到Home页面
         */
        void switchHome();

        void switchSections();

        void switchSection(SectionBean bean);
    }
}
