package com.zok.art.zhihu.ui.detail;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.ui.comment.CommentActivity;
import com.zok.art.zhihu.utils.ToastUtil;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class NewsDetailActivity extends BaseActivity<NewsDetailContract.Presenter>
        implements NewsDetailContract.View, View.OnClickListener {


    @BindView(R.id.wv_news_detail)
    protected WebView mWebView;             // 加载Html的Web View控件

    @BindView(R.id.detail_toolbar)
    protected Toolbar mToolbar;             // 页面顶部ToolBar控件

    @BindView(R.id.head_image)
    protected ImageView mHeadImage;         // AppLayout内部的文章主题插图

    @BindView(R.id.tv_big_title)         // 文章标题，位于AppLayout中心位置
    protected TextView mBigTitle;

    @BindView(R.id.tv_copyright)
    protected TextView mCopyRight;          // 插图版权信息，位于AppLayout右下角

    @BindView(R.id.scroll_view)
    protected NestedScrollView mNestedScrollView; // 嵌套的ScrollView对象，实现折叠的ToolBar效果

    @BindView(R.id.collapsing_toolbar_layout)
    protected CollapsingToolbarLayout mCollapsing;// 用于实现折叠的ToolBar效果

    @BindView(R.id.load_progress_bar)
    protected ProgressBar mLoadProgressBar;

    // action menu
    @BindView(R.id.menu_share)
    protected ImageView mMenuShare;
    @BindView(R.id.menu_collect)
    protected ImageView mMenuCollect;
    @BindView(R.id.menu_comment)
    protected ImageView mMenuComment;
    @BindView(R.id.menu_praise)
    protected ImageView mMenuPraise;
    @BindView(R.id.comment_count)
    protected TextView mCommentCount;
    @BindView(R.id.praise_count)
    protected TextView mPraiseCount;
    @BindView(R.id.tv_toolbar_title)
    protected TextView mToolBarTitle;
    private int scrollDis = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void setWindowFeature() {
        // 禁用沉浸式
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 实现页面导航
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void requestPermissionSucceed() {
        initDecorate();
        configWebView();
        setListener();
        // 启动P层逻辑
        mPresenter.start();
    }

    // 初始化ToolBar等系统装饰
    private void initDecorate() {
        // init toolbar
        mToolbar.setAlpha(0);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // 初始化ActionView
        mCommentCount.setText("...");
        mPraiseCount.setText("...");
    }

    // 配置WebView参数
    private void configWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setDayOrNight(!isNightMode());
    }

    // 设置相关监听器
    private void setListener() {
        // set webView Client
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mLoadProgressBar != null)
                    mLoadProgressBar.setVisibility(View.GONE);
            }
        });

        // set webView chrome client
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mLoadProgressBar != null)
                    mLoadProgressBar.setProgress(newProgress);
            }
        });


        // ToolBar渐变效果
        mNestedScrollView.setOnScrollChangeListener(
                new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = mHeadImage.getHeight();
                scrollDis += scrollY - oldScrollY;
                float rate = scrollDis * 1.0f / height;
                mToolbar.setAlpha(rate);
                // hide title
                if (rate >= 0)
                    mBigTitle.setVisibility(View.INVISIBLE);
                else if (rate < 0) {
                    mBigTitle.setVisibility(View.VISIBLE);
                }

                scrollDis = scrollDis < 0 ? 0 : scrollDis;
                scrollDis = scrollDis > height ? height : scrollDis;
            }
        });

        // 分享
        mMenuShare.setOnClickListener(this);
        // 收藏
        mMenuCollect.setOnClickListener(this);
        // 评论
        mMenuComment.setOnClickListener(this);
        // 点赞
        mMenuPraise.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_enter_anim, R.anim.trans_exit_anim);
    }

    private void goToCommentActivity() {
        long newsId = mPresenter.getNewsId();
        NewsExtraBean extraBean = mPresenter.getNewsExtraInfo();
        if (extraBean == null) {
            log.d("评论信息未成功加载");
            return;
        }

        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(CommentActivity.EXTRA_NEWS_ID, newsId);
        intent.putExtra(CommentActivity.EXTRA_COMMENT_INFO, extraBean);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_enter_anim, R.anim.trans_exit_anim);
    }

    @Override
    public void showProgress() {
        mLoadProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_share:
                break;
            case R.id.menu_collect:
                break;
            case R.id.menu_comment:
                goToCommentActivity();
                break;
            case R.id.menu_praise:
                break;
        }
        v.setSelected(!v.isSelected());
    }

    @Override
    public void showError(String msg, Throwable e) {
        e.printStackTrace();
        ToastUtil.show(this, msg);
        log.d(msg + ":" + e.toString());
    }

    public void updateHeadImage(String url) {
        Picasso.with(this).load(url).into(mHeadImage);
    }

    @Override
    public void updateTitle(String title) {
        mCollapsing.setTitle(title);
        mToolBarTitle.setText(title);
        mBigTitle.setText(title);
    }

    public void updateCopyRight(String copyright) {
        mCopyRight.setText(copyright);
    }

    @Override
    public void updateWebView(String html) {
        mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @Override
    public void updateExtra(NewsExtraBean bean) {
        mCommentCount.setText(String.valueOf(bean.getComments()));
        mPraiseCount.setText(String.valueOf(bean.getPopularity()));
    }


}
