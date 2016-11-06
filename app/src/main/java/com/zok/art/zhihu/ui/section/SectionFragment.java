package com.zok.art.zhihu.ui.section;

import android.os.Bundle;
import android.view.View;

import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.SectionNewsBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.refresh.RefreshFragment;

public class SectionFragment extends RefreshFragment<SectionNewsBean, SectionPresenter> {

    public static SectionFragment newInstance(SectionBean bean) {
        if (bean == null) {
            throw new RuntimeException("bean params must not is null");
        }
        Bundle args = new Bundle();
        SectionFragment fragment = new SectionFragment();
        args.putParcelable(Constants.EXTRA_INIT_PARAMS, bean);
        fragment.setArguments(args);
        return fragment;
    }

    public SectionPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getHeaderViewLayoutId() {
        return -1;
    }

    @Override
    protected void initHeaderView(View headerView) {
    }

    @Override
    public void updateBanner(SectionNewsBean storiesBean) {
    }

    @Override
    protected void onHeaderClick() {
    }
}
