package com.zok.art.zhihu.base;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

    /*日间/夜景 模式记录键值*/
    private static final String DAY_NIGHT_MODE = "day_night_mode";

    /*运行时权限成功授权码*/
    private static final int GRAND = PackageManager.PERMISSION_GRANTED;

    /*日志打印对象*/
    protected final LogUtil log = LogUtil.getLogUtil(this.getClass());

    /**
     * PRESENTER层对象
     */
    @Inject
    protected T mPresenter;

    /**
     * 控件解绑对象
     */
    private Unbinder mUnBinder;

    /**
     * 沉浸式标志
     */
    private boolean isImmersion;
    private View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. 初始化窗口参数
        initWindowParams();

        // 2. 初始化窗口布局
        initWindowUI();

        // 4. 注入PRESENTER层
        initInject();

        // 5. 请求权限
        requestPermissions();
    }

    /*初始化窗口参数*/
    protected void initWindowParams() {
    }


    /*初始化窗口布局*/
    private void initWindowUI() {
        // 设置布局
        mView = getLayoutInflater().inflate(getLayoutResId(), null);
        setContentView(mView);

        // 绑定控件
        mUnBinder = ButterKnife.bind(this);
    }

    /**
     * 子类必须实现该方法并返回当前Activity的布局ID
     *
     * @return 布局资源ID
     */
    protected abstract int getLayoutResId();

    /**
     * 子类应该重载该方法并添加注入代码
     */
    protected void initInject() {
    }

    /**
     * Dagger2注入工具对象
     *
     * @return Activity注入工具
     */
    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder().activityModule(
                new ActivityModule(this, getIntent())).build();
    }

    /*请求权限*/
    private void requestPermissions() {
        // 获得权限字符串数组
        final String[] permissions = AppUtil.getStrArr(getPermissionArrId());

        // 判断系统是不是大于等于M
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

        // 检查权限请求结果
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != GRAND)
                failed.add(permissions[i]);
        }

        // 报告每个请求失败的权限
        if (!failed.isEmpty()) {
            requestPermissionFailed(failed);
            return;
        }

        // 权限请求成功
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

    /**
     * 日间和夜景模式切换
     */
    protected void switchNightMode() {
        boolean isNight = !(Boolean) AppUtil.getGlobal(DAY_NIGHT_MODE, false);
        setDefaultNightMode(isNight ? MODE_NIGHT_YES : MODE_NIGHT_NO);
        AppUtil.putGlobal(DAY_NIGHT_MODE, isNight);
        recreate();
    }

    /**
     * 判断当前的模式
     *
     * @return true表示夜景模式
     */
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
    public void showError(String msg, Throwable e) {
        log.d(msg + ":" + e.getMessage());
        Snackbar.make(mView, msg, 2000).setAction("重试",
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.start();
            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
        mUnBinder.unbind();
    }
}
