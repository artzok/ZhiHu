package com.zok.art.zhihu.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.db.RealmManager;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.SPUtil;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;
import static android.support.v7.app.AppCompatDelegate.setDefaultNightMode;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class BaseApplication extends Application {
    public static RefWatcher sWatcher;
    @Override
    public void onCreate() {
        super.onCreate();

        // init app utils
        AppUtil.initGlobal(this);

        // add leak canary
        initLeakCanary();

        // realm init
        RealmManager.initRealm(this);

        // init qqx5
        initTbs();

        // init crash
        initCrashHandler();

        // init theme
        iniTheme();
    }

    private void iniTheme() {
        boolean isNight = SPUtil.getBoolean(Constants.DAY_NIGHT_MODE);
        setDefaultNightMode(isNight ? MODE_NIGHT_YES : MODE_NIGHT_NO);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        sWatcher = LeakCanary.install(this);
    }

    private void initCrashHandler() {
        // CrashReport.initCrashReport(getApplicationContext(), "900058880", true);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppVersion(String.valueOf(AppUtil.getAppVersion(this)));
        CrashReport.initCrashReport(getApplicationContext(), "900058880", true, strategy);
    }

    private void initTbs() {
        final QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            public void onViewInitFinished(boolean arg0) {}
            public void onCoreInitFinished() {}
        };
        QbSdk.setTbsListener(new TbsListener() {
            public void onDownloadFinish(int i) {}
            public void onInstallFinish(int i) {}
            public void onDownloadProgress(int i) {}
        });
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        QbSdk.preInit(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
