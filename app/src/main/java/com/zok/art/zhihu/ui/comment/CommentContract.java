package com.zok.art.zhihu.ui.comment;

import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;
import com.zok.art.zhihu.bean.CommentItemBean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface CommentContract {
    interface View extends BaseView {
        void updateTitle(String title);

        void updateLongComment(int longCount, int shortCount, List<CommentItemBean> comments);

        void showProgressBar();

        void closeProgressBar();
    }

    interface Presenter extends BasePresenter<View> {
        void loadLongComment();

        void loadOrDeleteShortComment();
    }

}
