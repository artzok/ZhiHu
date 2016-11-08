package com.zok.art.zhihu.ui.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.BasicStoryBean;
import com.zok.art.zhihu.bean.NewsDetailBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.ui.comment.CommentActivity;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.IOUti;
import com.zok.art.zhihu.utils.ImageLoaderManager;
import com.zok.art.zhihu.utils.StringUtil;
import com.zok.art.zhihu.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class DetailActivity extends BaseActivity<DetailContract.Presenter>
        implements DetailContract.View {
    @BindView(R.id.wv_news_detail)
    protected WebView mWebView;             // WebView控件

    @BindView(R.id.detail_toolbar)
    protected Toolbar mToolbar;             // ToolBar控件

    @BindView(R.id.header_image)            // 主题插图
    protected ImageView mHeadImage;

    @BindView(R.id.tv_headline)             // 文章大标题
    protected TextView mHeadline;

    @BindView(R.id.tv_copyright)            // 版权信息
    protected TextView mCopyRight;

    @BindView(R.id.load_progress_bar)       // 进度条
    protected ProgressBar mLoadProgressBar;

    @BindView(R.id.comment_count)           // 评论数
    protected TextView mCommentCount;

    @BindView(R.id.praise_count)            // 点赞数
    protected TextView mPraiseCount;

    @BindView(R.id.menu_collect)            // 收藏
    protected ImageView mMenuCollect;

    @BindView(R.id.menu_praise)             // 点赞
    protected ImageView mMenuPraise;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    protected void requestPermissionSucceed() {
        initDecorate();
        configWebView();
        setListener();
        mPresenter.start();
    }

    private void initDecorate() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // 初始化ActionView
        mCommentCount.setText("...");
        mPraiseCount.setText("...");
        // set collect state and praise state
        mMenuCollect.setSelected(mPresenter.isCollected());
        mMenuPraise.setSelected(mPresenter.isPraised());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setDayOrNight(!isNightMode());
    }

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

    @OnClick({R.id.menu_share, R.id.menu_collect, R.id.menu_comment, R.id.menu_praise})
    public void onActionMenuClick(View v) {
        switch (v.getId()) {
            case R.id.menu_share:
                shareDetail();
                break;
            case R.id.menu_collect:
                collectDetail(v);
                break;
            case R.id.menu_comment:
                goToCommentActivity();
                break;
            case R.id.menu_praise:
                praiseDetail(v);

                break;
        }
        v.setSelected(!v.isSelected());
    }


    private void shareDetail() {
        final String shareTitle = mPresenter.getStoryBean().getTitle();         // 分享窗口标题
        final String detailTitle = mPresenter.getNewsDetail().getTitle();       // 文章标题
        final String imageUrl = mPresenter.getNewsDetail().getLagerImageUrl();  // 图片URL
        final String detailUrl = mPresenter.getNewsDetail().getShareUrl();      // 文章内容
        ImageLoaderManager.saveShareImage(imageUrl, new ImageLoaderManager.SaveImageCallBack() {
            @Override
            public void callBack(File file) {
                startActivity(AppUtil.createShareIntent(shareTitle, detailTitle, detailUrl, file));
            }
        });
    }

    private void collectDetail(View v) {
        mPresenter.setCollected(!mPresenter.isCollected());
        if (v.isSelected()) {
            ToastUtil.show(this, "取消成功", R.drawable.collect);
        } else {
            ToastUtil.show(this, "收藏成功", R.drawable.collected);
        }
    }

    private void praiseDetail(View v) {
        // TODO: 2016/11/8 点赞
        mPresenter.setPraised(!mPresenter.isPraised());
        if (v.isSelected()) {
            ToastUtil.show(this, "取消点赞", R.drawable.praise);
        } else {
            ToastUtil.show(this, "点赞成功", R.drawable.praised);
        }
    }

    private void goToCommentActivity() {
        long newsId = mPresenter.getStoryBean().getId();
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
    public void closeProgress() {
        mLoadProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateHeaderImage(String url) {
        ImageLoaderManager.loadHeaderImage(this, mHeadImage, url);
    }

    @Override
    public void updateTitle(String title) {
        mHeadline.setText(title);
    }

    @Override
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
