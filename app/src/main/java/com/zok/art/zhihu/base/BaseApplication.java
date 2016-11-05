package com.zok.art.zhihu.base;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.zok.art.zhihu.db.RealmManager;
import com.zok.art.zhihu.utils.AppUtil;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        // add leak canary
        sWatcher = LeakCanary.install(this);

        // realm init
        RealmManager.initRealm(this);

        // init qqx5
        initTbs();
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
}
