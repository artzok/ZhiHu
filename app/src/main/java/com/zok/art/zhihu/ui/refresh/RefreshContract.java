package com.zok.art.zhihu.ui.refresh;

import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;
import com.zok.art.zhihu.bean.ListStoryBean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface RefreshContract {

    interface View<M> extends BaseView {
        void openRefreshUI();

        void closeRefreshUI();

        void updateHeaderView(M mode);

        void updateNewsList(List<ListStoryBean> bean);
    }

    interface Presenter<M, V extends View<M>> extends BasePresenter<V> {
        void pause();

        void resume();

        void updateLatest();

        void updateBefore();

        List<ListStoryBean> getListBeans(M mode);

        String getTitle();

        void setReadState(ListStoryBean bean);
    }
}
