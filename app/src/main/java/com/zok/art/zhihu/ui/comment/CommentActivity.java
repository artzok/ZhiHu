package com.zok.art.zhihu.ui.comment;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.CommentListAdapter;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.CommentItemBean;

import java.util.List;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class CommentActivity extends BaseActivity<CommentContract.Presenter>
        implements CommentContract.View, AdapterView.OnItemClickListener {
    public static final String EXTRA_NEWS_ID = "extra_news_id";
    public static final String EXTRA_COMMENT_INFO = "extra_comment_info";

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.lv_comments)
    protected ListView mCommentsList;

    @BindView(R.id.load_progress_bar)
    protected ProgressBar mLoadProgress;

    private CommentListAdapter mCommentListAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void requestPermissionSucceed() {
//        requestImmersion();
        initDecorate();
        initAdapter();
        initListener();
        mPresenter.start();
    }

    private void initDecorate() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initAdapter() {
        mCommentListAdapter = new CommentListAdapter(this);
        mCommentsList.setAdapter(mCommentListAdapter);
    }


    private void initListener() {
        mCommentsList.setOnItemClickListener(this);
    }

    /*---------------------------Action Menu相关--------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
    /*-------------------------------------------------------------------------*/

    @Override
    public void updateTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void updateLongComment(int longCount, int shortCount, List<CommentItemBean> comments) {
        mCommentListAdapter.setLongCount(longCount);
        mCommentListAdapter.setShortCount(shortCount);
        mCommentListAdapter.setDataAndRefresh(comments);
        mCommentsList.post(new Runnable() {
            @Override
            public void run() {
                mCommentsList.smoothScrollToPositionFromTop(mCommentListAdapter.getLongCount() + 1, 0, 500);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_enter_anim, R.anim.trans_exit_anim);
    }

    @Override
    public void showProgressBar() {
        mLoadProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeProgressBar() {
        mLoadProgress.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (mCommentsList.isInLayout())
                return;
        }

        if (mCommentListAdapter.getItemViewType(position) == CommentListAdapter.ITEM_TYPE_SHORT) {
            if (mCommentListAdapter.isFinishOfAnimation()) {
                mPresenter.loadOrDeleteShortComment();
                mCommentListAdapter.animate();
            }
        }
    }
}
