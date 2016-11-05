package com.zok.art.zhihu.ui.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;

import com.art.zok.autoview.AutoViewPager;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.BannerPageAdapter;
import com.zok.art.zhihu.bean.StoriesLatestBean;
import com.zok.art.zhihu.bean.StoryBannerItemBean;
import com.zok.art.zhihu.ui.refresh.RefreshFragment;

public class HomeFragment extends RefreshFragment<StoriesLatestBean, HomeContract.Presenter>
        implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener {
    // header view : image banner
    private AutoViewPager mBannerView;
    private BannerPageAdapter mBannerAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getHeaderViewLayoutId() {
        return R.layout.home_banner;
    }

    @Override
    protected void initHeaderView(View headerView) {
        // create adapter for banner
        mBannerView = (AutoViewPager) headerView.findViewById(R.id.home_banner_view);
        mBannerAdapter = new BannerPageAdapter(getActivity());
        mBannerView.setPagerAdapter(mBannerAdapter);
        // start auto play
        mBannerView.start();
    }

    @Override
    public void updateBanner(StoriesLatestBean storiesBean) {
        mBannerAdapter.setDataAndRefresh(storiesBean.getBannerStories());
    }

    @Override
    protected void onHeaderClick() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBannerView != null)
            mBannerView.play();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerView != null)
            mBannerView.pause();
    }
}
