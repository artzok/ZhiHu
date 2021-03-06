package com.zok.art.zhihu.ui.refresh;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.RefreshListAdapter;
import com.zok.art.zhihu.base.BaseApplication;
import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.bean.StoryListItemBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class RefreshFragment<M, P extends RefreshContract.Presenter>
        extends BaseFragment<P> implements RefreshContract.View<M>,
        SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener {

    @BindView(R.id.pull_refresh_view)
    protected SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.lv_news)
    protected ListView mNewsListView;

    // adapter
    private RefreshListAdapter mNewsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_refresh;
    }

    @Override
    protected void initialize() {
        initSubView();
        // start presenter logic
        mPresenter.start();
    }

    private void initSubView() {
        int id = getHeaderViewLayoutId();
        if (id != -1) {
            View headerView = getActivity().getLayoutInflater().inflate(id, null);
            // 子类初始化headerView控件
            initHeaderView(headerView);
            mNewsListView.addHeaderView(headerView);
        }

        // last news item adapter
        mNewsAdapter = new RefreshListAdapter(getActivity());
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
    public void updateNewsList(List<StoryListItemBean> bean) {
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
        StoryListItemBean bean = (StoryListItemBean) mNewsAdapter.getItem(firstVisiblePosition);
        if (bean.isDate()) {
            updateTitle(bean.getDateString());
        } else if (firstVisibleItem == 0) {
            updateTitle(mPresenter.getTitle());
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
        StoryListItemBean item = (StoryListItemBean) mNewsAdapter.getItem(position - count);
        // filter decorate item
        if (!item.isDate() && !item.isTitle()) {
            // mark unread news
            if (!item.isRead()) {
                item.setRead(true);
                mPresenter.setReadState(item);
                mNewsAdapter.notifyDataSetChanged();
            }
            // go to detail activity
            startDetailActivity(item);
        }
    }

    protected void onHeaderClick() {
    }

    protected void startDetailActivity(StoryListItemBean bean) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.EXTRA_INIT_PARAMS, bean);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_enter_anim, R.anim.trans_exit_anim);
    }
}
