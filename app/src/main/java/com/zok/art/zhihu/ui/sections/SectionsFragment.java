package com.zok.art.zhihu.ui.sections;

import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.inter.ICardItem;
import com.zok.art.zhihu.ui.card.CardFragment;
import com.zok.art.zhihu.ui.main.MainActivity;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionsFragment extends CardFragment<SectionsPresenter> {

    public static SectionsFragment newInstance() {
        return new SectionsFragment();
    }

    @Override
    public void onItemClick(MainActivity activity, ICardItem iCardItem) {
        SectionBean item = (SectionBean) iCardItem;
        activity.goSection(item);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }
}
