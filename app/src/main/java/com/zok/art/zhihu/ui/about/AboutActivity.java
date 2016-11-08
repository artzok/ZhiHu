package com.zok.art.zhihu.ui.about;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.utils.AppUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.version)
    public TextView mVersionText;
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
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AboutActivity.this.finish();
                }
            });
            actionBar.setTitle(R.string.about);
        }

        // enable title
        mCollapsingToolbarLayout.setTitleEnabled(false);
        mCollapsingToolbarLayout.setExpandedTitleGravity(GravityCompat.START);

        // set version
        String version = getString(R.string.version_info) + AppUtil.getAppVersion(this);
        mVersionText.setText(version);
    }

    @OnClick({R.id.weibo_link, R.id.github_link})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weibo_link:
                break;
            case R.id.github_link:
                break;

            default:
                break;
        }
    }

}

