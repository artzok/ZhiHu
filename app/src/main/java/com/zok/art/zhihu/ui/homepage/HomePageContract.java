package com.zok.art.zhihu.ui.homepage;

import com.zok.art.zhihu.bean.LatestStoriesBean;
import com.zok.art.zhihu.ui.refresh.RefreshContract;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface HomePageContract {
    interface View extends RefreshContract.View<LatestStoriesBean> {
    }

    interface Presenter extends RefreshContract.Presenter<LatestStoriesBean, View> {
    }
}
