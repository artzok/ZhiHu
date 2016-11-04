package com.zok.art.zhihu.base;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.di.component.ActivityComponent;
import com.zok.art.zhihu.di.component.DaggerActivityComponent;
import com.zok.art.zhihu.di.module.ActivityModule;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.LogUtil;
import com.zok.art.zhihu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;
import static android.support.v7.app.AppCompatDelegate.setDefaultNightMode;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class BaseActivity<T extends BasePresenter>
        extends AppCompatActivity implements BaseView {
    private static final String DAY_NIGHT_MODE = "day_night_mode";
    private static final int GRAND = PackageManager.PERMISSION_GRANTED;
    protected final LogUtil log = LogUtil.getLogUtil(this.getClass());

    @Inject
    protected T mPresenter;

    private Unbinder mUnBinder;

    private boolean isImmersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. 处理屏幕状态参数
        setWindowFeature();
        // 2. 初始化窗口布局
        initWindowUI();
        // 4.  注入P层
        initInject();
        //请求权限
        requestPermissions();
    }

    protected void setWindowFeature() {
        requestImmersion();
    }


    protected void initWindowUI() {
        // 设置内容布局
        setContentView(getLayoutResId());
        // 绑定所有控件
        mUnBinder = ButterKnife.bind(this);
    }

    protected abstract int getLayoutResId();

    protected abstract void initInject();

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder().activityModule(
                new ActivityModule(this, getIntent())).build();
    }

    private void requestPermissions() {
       final String[] permissions = AppUtil.getStrArr(getPermissionArrId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean result = true;
            for (String permission : permissions) {
                if (GRAND != ActivityCompat.checkSelfPermission(this, permission)) {
                    result = false;
                    break;
                }
            }
            if (result) {
                requestPermissionSucceed();
            } else {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ActivityCompat.requestPermissions(BaseActivity.this, permissions, 0);
                    }
                });
            }
        } else {
            requestPermissionSucceed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> failed = new ArrayList<>();
        // check permission and add filed item
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != GRAND)
                failed.add(permissions[i]);
        }
        // notify subclass request failed
        if (!failed.isEmpty()) {
            requestPermissionFailed(failed);
            return;
        }
        requestPermissionSucceed();
    }

    /**
     * 权限请求成功时调用
     */
    protected abstract void requestPermissionSucceed();

    /**
     * 权限请求失败时调用
     */
    protected void requestPermissionFailed(List<String> failedPermissions) {
        ToastUtil.show(this, failedPermissions.toString());
        finish();
    }

    /**
     * 返回权限数组资源id
     */
    protected int getPermissionArrId() {
        return R.array.basic_permissions;
    }

    public void switchNightMode() {
        boolean isNight = !(Boolean) AppUtil.getGlobal(DAY_NIGHT_MODE, false);
        setDefaultNightMode(isNight ? MODE_NIGHT_YES : MODE_NIGHT_NO);
        AppUtil.putGlobal(DAY_NIGHT_MODE, isNight);
        recreate();
    }

    protected boolean isNightMode() {
        return (boolean) AppUtil.getGlobal(DAY_NIGHT_MODE, false);
    }

    /**
     * 请求沉浸式
     */
    protected void requestImmersion() {
        isImmersion = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isImmersion) return;
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
        mUnBinder.unbind();
    }
}
