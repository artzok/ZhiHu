package com.zok.art.zhihu.ui.setting;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.kyleduo.switchbutton.SwitchButton;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.setting_offline_download_sb)
    public SwitchButton offline_download_sb;

    @BindView(R.id.setting_no_image_sb)
    public SwitchButton no_image_sb;

    @BindView(R.id.setting_big_text_sb)
    public SwitchButton big_text_sb;

    @BindView(R.id.setting_propelling_sb)
    public SwitchButton propelling_sb;

    @BindView(R.id.setting_share_weibo_sb)
    public SwitchButton share_weibo_sb;

    @BindView(R.id.rl_setting_offline_download)
    public RelativeLayout offline_download;

    @BindView(R.id.rl_setting_no_image)
    public RelativeLayout no_image;

    @BindView(R.id.rl_setting_big_text)
    public RelativeLayout big_text;

    @BindView(R.id.rl_setting_propelling)
    public RelativeLayout propelling;

    @BindView(R.id.rl_setting_share_weibo)
    public RelativeLayout share_weibo;

    @BindView(R.id.rl_setting_clear_cache)
    public RelativeLayout clear_cache;

    @BindView(R.id.rl_setting_check_update)
    public RelativeLayout check_update;

    @BindView(R.id.rl_setting_feedback_msg)
    public RelativeLayout feedback_msg;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;   // 返回布局ID
    }

    @Override
    protected void requestPermissionSucceed() {

        initData();
        initUI();
    }

    private void initUI() {
        setToolBar(mToolbar, AppUtil.getString(R.string.menu_setting_tips), true);
        offline_download.setOnClickListener(this);
        no_image.setOnClickListener(this);
        big_text.setOnClickListener(this);
        propelling.setOnClickListener(this);
        share_weibo.setOnClickListener(this);
        clear_cache.setOnClickListener(this);
        check_update.setOnClickListener(this);
        feedback_msg.setOnClickListener(this);
    }

    private void initData() {
        offline_download_sb.setCheckedImmediately(SPUtil.getBoolean(Constants.SETTING_AUTO_DOWNLOAD));
        no_image_sb.setCheckedImmediately(SPUtil.getBoolean(Constants.SETTING_NO_IMAGE));
        big_text_sb.setCheckedImmediately(SPUtil.getBoolean(Constants.SETTING_BIG_FONT));
        propelling_sb.setCheckedImmediately(SPUtil.getBoolean(Constants.SETTING_PUSH_MESSAGE));
        share_weibo_sb.setCheckedImmediately(SPUtil.getBoolean(Constants.SETTING_SHARE_SINA_WEIBO));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting_offline_download:
                offline_download_sb.toggle();
                SPUtil.putBoolean(Constants.SETTING_AUTO_DOWNLOAD,  !SPUtil.getBoolean(Constants.SETTING_AUTO_DOWNLOAD));
                break;
            case R.id.rl_setting_no_image:
                no_image_sb.toggle();
                SPUtil.putBoolean(Constants.SETTING_NO_IMAGE, !SPUtil.getBoolean(Constants.SETTING_NO_IMAGE));
                break;
            case R.id.rl_setting_big_text:
                big_text_sb.toggle();
                SPUtil.putBoolean(Constants.SETTING_BIG_FONT,  !SPUtil.getBoolean(Constants.SETTING_BIG_FONT));
                break;
            case R.id.rl_setting_propelling:
                propelling_sb.toggle();
                SPUtil.putBoolean(Constants.SETTING_PUSH_MESSAGE, propelling_sb.isChecked());
                break;
            case R.id.rl_setting_share_weibo:
                share_weibo_sb.toggle();
                SPUtil.putBoolean(Constants.SETTING_SHARE_SINA_WEIBO, share_weibo_sb.isChecked());
                break;
            case R.id.rl_setting_clear_cache:
                //SPUtil.putBoolean(Constants.SETTING_AUTO_DOWNLOAD,true);
                break;
            case R.id.rl_setting_check_update:
                //SPUtil.putBoolean(Constants.SETTING_AUTO_DOWNLOAD,true);
                break;
            case R.id.rl_setting_feedback_msg:
                //SPUtil.putBoolean(Constants.SETTING_AUTO_DOWNLOAD,true);
                break;
        }
    }
}