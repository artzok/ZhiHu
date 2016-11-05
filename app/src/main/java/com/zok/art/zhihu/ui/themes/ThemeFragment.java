package com.zok.art.zhihu.ui.themes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.bean.ThemeNewsBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.refresh.RefreshContract;
import com.zok.art.zhihu.ui.refresh.RefreshFragment;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemeFragment extends
        RefreshFragment<ThemeNewsBean, ThemePresenter>
       {

    private ImageView mThemePageHeader;

    public static ThemeFragment newInstance(ThemeItemBean bean) {
        if (bean == null) {
            throw new RuntimeException("bean params must not is null");
        }
        Bundle args = new Bundle();
        ThemeFragment fragment = new ThemeFragment();
        args.putParcelable(Constants.EXTRA_INIT_PARAMS, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getHeaderViewLayoutId() {
        return R.layout.theme_page_header;
    }

    @Override
    protected void initHeaderView(View headerView) {
        mThemePageHeader = (ImageView)
                headerView.findViewById(R.id.theme_page_header);
    }

    @Override
    public void updateBanner(ThemeNewsBean storiesBean) {
        Picasso.with(getContext()).load(storiesBean.getImage()).into(mThemePageHeader);
    }
}
