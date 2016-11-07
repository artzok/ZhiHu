package com.zok.art.zhihu.ui.card;

import com.zok.art.zhihu.base.BaseFragmentContract;
import com.zok.art.zhihu.inter.ICardItem;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface CardContract {
    interface View extends BaseFragmentContract.View {
        void updateCards(List<? extends ICardItem> data);
        void updateTitle(String title);
    }
    interface Presenter extends BaseFragmentContract.Presenter<View> {
        void loadCards();
        String getTitle();
    }

}
