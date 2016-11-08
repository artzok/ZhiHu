package com.zok.art.zhihu.ui.collected;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.CollectedAdapter;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.db.RealmManager;
import com.zok.art.zhihu.db.bean.ReadStateBean;
import com.zok.art.zhihu.ui.detail.DetailActivity;
import com.zok.art.zhihu.utils.AppUtil;

import java.util.List;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class CollectedActivity extends BaseActivity
        implements AdapterView.OnItemClickListener {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.lv_collects)
    public ListView mCollectsView;

    @BindView(R.id.empty_view)
    public View mEmptyView;

    private CollectedAdapter mAdapter;
    private List<ReadStateBean> mBeen;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_collected;
    }

    @Override
    protected void requestPermissionSucceed() {
        // set action bar
        mToolbar.setTitle("收藏");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // set click event
        mCollectsView.setOnItemClickListener(this);

        // set adapter
        mAdapter = new CollectedAdapter(this);
        mCollectsView.setAdapter(mAdapter);

        // update content
        updateContent();
    }

    private void updateContent() {
        // query records
        RealmManager instance = RealmManager.getInstance();
        mBeen = instance.queryAllCollected();
        updateEmptyView();

        // set and update
        mAdapter.setDataAndRefresh(mBeen);
    }

    private void updateEmptyView() {
        // hide empty view
        if (mBeen == null || mBeen.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ReadStateBean item = (ReadStateBean) mAdapter.getItem(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_INIT_PARAMS, RealmManager.getListItem(item));
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.notifyDataSetChanged();
        updateEmptyView();
    }
}
