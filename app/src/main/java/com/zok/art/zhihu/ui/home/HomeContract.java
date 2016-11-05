package com.zok.art.zhihu.ui.home;

import com.zok.art.zhihu.bean.StoriesLatestBean;
import com.zok.art.zhihu.ui.refresh.RefreshContract;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface HomeContract {
    interface View extends RefreshContract.View<StoriesLatestBean> {
    }

    interface Presenter extends RefreshContract.Presenter<StoriesLatestBean, View> {
    }
}
