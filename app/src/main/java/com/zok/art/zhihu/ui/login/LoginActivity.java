package com.zok.art.zhihu.ui.login;

import android.support.v7.widget.Toolbar;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.utils.AppUtil;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;// 返回布局ID
    }

    @Override
    protected void requestPermissionSucceed() {
        setToolBar(mToolbar, AppUtil.getString(R.string.login_tip), true);
        // TODO: 2016/11/8 登录功能
        // 代码执行起点
    }
}
