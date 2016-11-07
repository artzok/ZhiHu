package com.zok.art.zhihu.ui.themes;

import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.inter.ICardItem;
import com.zok.art.zhihu.ui.card.CardFragment;
import com.zok.art.zhihu.ui.main.MainActivity;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemesFragment extends CardFragment<ThemesPresenter> {
    public static ThemesFragment newInstance() {
        return new ThemesFragment();
    }

    @Override
    public void onItemClick(MainActivity activity, ICardItem iCardItem) {
        ThemeItemBean item = (ThemeItemBean) iCardItem;
        activity.goTheme(item);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }
}
