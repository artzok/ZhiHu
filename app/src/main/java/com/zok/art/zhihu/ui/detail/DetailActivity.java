package com.zok.art.zhihu.ui.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.comment.CommentActivity;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.ImageLoaderManager;
import com.zok.art.zhihu.utils.NetWorkUtil;
import com.zok.art.zhihu.utils.SPUtil;
import com.zok.art.zhihu.utils.ToastUtil;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.tencent.smtt.sdk.WebSettings.*;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class DetailActivity extends BaseActivity<DetailContract.Presenter>
        implements DetailContract.View {
    @BindView(R.id.wv_news_detail_container)
    public FrameLayout mWebViewContainer;   // webView容器

    protected WebView mWebView;             // WebView控件

    @BindView(R.id.toolbar)
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
    protected void requestPermissionSucceed() {
        initDecorate();
        configWebView();
        setListener();
        mPresenter.start();
    }

    private void initDecorate() {
        setToolBar(mToolbar, " ", true);
        // 初始化ActionView
        mCommentCount.setText("...");
        mPraiseCount.setText("...");
        // set collect state and praise state
        mMenuCollect.setSelected(mPresenter.isCollected());
        mMenuPraise.setSelected(mPresenter.isPraised());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configWebView() {
        // must is app context, otherwise will may leak memory
        mWebView = new WebView(getApplicationContext());
        mWebViewContainer.addView(mWebView);
        WebSettings settings = mWebView.getSettings();

        // 设置无图模式
        settings.setBlockNetworkImage(SPUtil.getBoolean(Constants.SETTING_NO_IMAGE));

        // 设置离线设置
        boolean offline = SPUtil.getBoolean(Constants.SETTING_AUTO_DOWNLOAD);
        settings.setAppCacheEnabled(offline);
        settings.setDomStorageEnabled(offline);
        settings.setDatabaseEnabled(offline);

        // 设置缓存模式
        boolean netWorkAvailable = NetWorkUtil.isNetWorkAvailable(AppUtil.getAppContext());
        settings.setCacheMode(netWorkAvailable ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ONLY);

        // 设置大号字体
        if (SPUtil.getBoolean(Constants.SETTING_BIG_FONT)) {
            settings.setMinimumFontSize(25);
        }

        // 激活java script
        settings.setJavaScriptEnabled(true);

        // 充满全屏
        settings.setLoadWithOverviewMode(true);

        // 适配移动端屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        // 设置可以访问文件内容
        settings.setAllowContentAccess(true);

        // 设置夜景模式
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
            case R.id.menu_share:   // 分享
                shareDetail();
                break;
            case R.id.menu_collect: // 收藏
                collectDetail(v);
                break;
            case R.id.menu_comment:     // 评论
                goToCommentActivity();
                break;
            case R.id.menu_praise:      // 点赞
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
        mPresenter.setPraised(!mPresenter.isPraised());
        if (v.isSelected()) {
            ToastUtil.show(this, "取消点赞", R.drawable.praise);
        } else {
            ToastUtil.show(this, "点赞成功", R.drawable.praised);
        }
        updateExtra(mPresenter.getNewsExtraInfo());
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
        int popularity = bean.getPopularity();
        if (mPresenter.isPraised()) {
            popularity += 1;
        }
        mPraiseCount.setText(String.valueOf(popularity));
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }
}
