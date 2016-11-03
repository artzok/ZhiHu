package com.zok.art.zhihu.base;

import android.app.Application;
import android.content.res.Configuration;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
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
//        sWatcher = LeakCanary.install(this);
        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults().build();
        sWatcher = LeakCanary.refWatcher(this).excludedRefs(excludedRefs).buildAndInstall();

        // realm init
        RealmManager.initRealm(this);
    }
}
