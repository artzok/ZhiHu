package com.zok.art.zhihu.ui.homepage;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;

import com.art.zok.autoview.AutoViewPager;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.HomePageBannerAdapter;
import com.zok.art.zhihu.bean.LatestStoriesBean;
import com.zok.art.zhihu.ui.refresh.RefreshFragment;

public class HomePageFragment extends RefreshFragment<LatestStoriesBean, HomePageContract.Presenter>
        implements HomePageContract.View, SwipeRefreshLayout.OnRefreshListener {
    // header view : image banner
    private AutoViewPager mHomePageHeader;
    private HomePageBannerAdapter mBannerAdapter;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getHeaderViewLayoutId() {
        return R.layout.home_page_header;
    }

    @Override
    protected void initHeaderView(View headerView) {
        // banner auto view pager adapter
        mHomePageHeader = (AutoViewPager) headerView.findViewById(R.id.home_page_banner);
        mBannerAdapter = new HomePageBannerAdapter(getActivity());
        mHomePageHeader.setPagerAdapter(mBannerAdapter);
        // start auto play
        mHomePageHeader.start();
        mHomePageHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


    }

    @Override
    public void updateHeaderView(LatestStoriesBean storiesBean) {
        mBannerAdapter.setDataAndRefresh(storiesBean.getTopStories());
    }

    @Override
    protected void onHeaderClick() {
        int item = mHomePageHeader.getCurrentItem();
        startDetailActivity(mBannerAdapter.getItem(item).getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHomePageHeader != null)
            mHomePageHeader.play();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mHomePageHeader != null)
            mHomePageHeader.pause();
    }
}
