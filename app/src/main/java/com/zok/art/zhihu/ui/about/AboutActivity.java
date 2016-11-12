package com.zok.art.zhihu.ui.about;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.version)
    public TextView mVersionText;

    @BindView(R.id.github_link)
    public TextView mGithubLink;

    @BindView(R.id.collapsing_toolbar)
    public CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void requestPermissionSucceed() {
        setupHeader();
    }


    private void setupHeader() {
        // action bar
        setToolBar(mToolbar, AppUtil.getString(R.string.about), true);

        // enable title
        mCollapsingToolbarLayout.setTitleEnabled(false);
        mCollapsingToolbarLayout.setExpandedTitleGravity(GravityCompat.START);

        // set version
        String version = getString(R.string.version_info) + AppUtil.getAppVersion(this);
        mVersionText.setText(version);

        // set github link
        mGithubLink.setText(StringUtil.getHtml(R.string.visit_github_detail));
        mGithubLink.setAutoLinkMask(Linkify.ALL);
        mGithubLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick(R.id.weixin_qr)
    public void onClick(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.qr, null);
        ImageView qrImage = (ImageView) view.findViewById(R.id.qr_image);
        qrImage.setImageResource(R.drawable.qr);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 500;
        params.height = 500;
        dialog.getWindow().setAttributes(params);
    }
}

