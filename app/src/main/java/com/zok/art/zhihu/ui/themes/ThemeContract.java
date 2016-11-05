package com.zok.art.zhihu.ui.themes;

import com.zok.art.zhihu.bean.ThemeNewsBean;
import com.zok.art.zhihu.ui.refresh.RefreshContract;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface ThemeContract {
    interface View extends RefreshContract.View<ThemeNewsBean> {

    }

    interface Presenter extends RefreshContract.Presenter<ThemeNewsBean, View> {
    }
}
