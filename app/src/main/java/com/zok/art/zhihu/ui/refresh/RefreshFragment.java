package com.zok.art.zhihu.ui.refresh;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.NewsListAdapter;
import com.zok.art.zhihu.base.BaseApplication;
import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.bean.ListStoryBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.main.MainActivity;
import com.zok.art.zhihu.ui.detail.NewsDetailActivity;
import com.zok.art.zhihu.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class RefreshFragment<M, P extends RefreshContract.Presenter>
        extends BaseFragment<P> implements RefreshContract.View<M>,
        SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    @BindView(R.id.pull_refresh_view)
    protected SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.lv_news)
    protected ListView mNewsListView;

    // adapter
    private NewsListAdapter mNewsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_refresh;
    }

    @Override
    public void showError(String msg, Throwable e) {
        ToastUtil.show(getActivity(), msg);
        log.e(msg + ":" + e.getMessage());
    }

    @Override
    protected void initialize() {
        initSubView();
        // start presenter logic
        mPresenter.start();
    }

    private void initSubView() {
        View headerView = getActivity().getLayoutInflater().inflate(getHeaderViewLayoutId(), null);
        // 子类初始化headerView控件
        initHeaderView(headerView);
        mNewsListView.addHeaderView(headerView);

        // last news item adapter
        mNewsAdapter = new NewsListAdapter(getActivity());
        mNewsListView.setAdapter(mNewsAdapter);
        mNewsListView.setOnScrollListener(this);
        mNewsListView.setOnItemClickListener(this);

        // set pull refresh UI's color style
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        mRefreshLayout.setOnRefreshListener(this);
    }

    protected abstract int getHeaderViewLayoutId();

    protected abstract void initHeaderView(View headerView);

    @Override
    public void updateNewsList(List<ListStoryBean> bean) {
        mNewsAdapter.setDataAndRefresh(bean);
    }

    @Override
    public void openRefreshUI() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void closeRefreshUI() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.updateLatest();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.resume();
        //mNewsListView.smoothScrollToPositionFromTop(0, 0, 200);
        mNewsListView.setSelection(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseApplication.sWatcher.watch(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = mNewsAdapter.getCount();
        if (count <= 0) return;

        // load before
        if (mNewsListView.getLastVisiblePosition() == count - 1) {
            mPresenter.updateBefore();
        }

        // modify action bar title
        int firstVisiblePosition = mNewsListView.getFirstVisiblePosition();
        ListStoryBean bean = (ListStoryBean) mNewsAdapter.getItem(firstVisiblePosition);
        MainActivity activity = (MainActivity) getActivity();
        if (bean.isDate()) {
            activity.updateActionBarTitle(bean.getDateString());
        } else if (firstVisibleItem == 0) {
            activity.updateActionBarTitle(mPresenter.getTitle());
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get header count
        int count = mNewsListView.getHeaderViewsCount();

        // click header
        if (position == 0) {
            onHeaderClick();
            return;
        }

        // click item
        ListStoryBean item = (ListStoryBean) mNewsAdapter.getItem(position - count);
        // filter decorate item
        if (!item.isDate() && !item.isTitle()) {
            // mark unread news
            if (!item.isRead()) {
                item.setRead(true);
                mPresenter.setReadState(item);
                mNewsAdapter.notifyDataSetChanged();
            }
            // go to detail activity
            startDetailActivity(item.getId());
        }
    }

    protected void onHeaderClick() {
    }

    protected void startDetailActivity(long id) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(Constants.EXTRA_INIT_PARAMS, id);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_enter_anim, R.anim.trans_exit_anim);
    }
}
