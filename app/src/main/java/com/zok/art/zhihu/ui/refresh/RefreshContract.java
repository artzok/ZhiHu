package com.zok.art.zhihu.ui.refresh;

import com.zok.art.zhihu.base.BaseFragmentContract;
import com.zok.art.zhihu.bean.StoryListItemBean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface RefreshContract {

    interface View<M> extends BaseFragmentContract.View {
        void openRefreshUI();

        void closeRefreshUI();

        void updateBanner(M mode);

        void updateNewsList(List<StoryListItemBean> bean);
    }

    interface Presenter<M, V extends View<M>> extends BaseFragmentContract.Presenter<V> {

        void updateLatest();

        void updateBefore();

        List<StoryListItemBean> getListBeans(M mode);

        void setReadState(StoryListItemBean bean);
    }
}
