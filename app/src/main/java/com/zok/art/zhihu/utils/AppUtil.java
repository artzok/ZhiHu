package com.zok.art.zhihu.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

/**
 * Created by artzok on 2016/9/24.
 * get global resources
 */
public class AppUtil {
    /* global application parameter */
    private static Context sContext;
    private static Handler sHandler;
    private static int sThreadId;
    private static Gson sGson;

    /* global cache */
    private static Map<String, Object> sCacheMap;

    public static void initGlobal(Context appCtx) {
        sContext = appCtx;
        sGson = new Gson();
        sHandler = new Handler();
        sThreadId = Process.myTid();
        sCacheMap = new HashMap<>();
    }

    public static final void putGlobal(String key, Object value) {
        sCacheMap.put(key, value);
    }

    public static final Object getGlobal(String key, Object defaultValue) {
        Object va = sCacheMap.get(key);
        if (va == null) return defaultValue;
        return va;
    }

    public static final DisplayMetrics getDisplayMetrics() {
        return getAppRes().getDisplayMetrics();
    }

    public static final String formatByteSize(long size) {
        return Formatter.formatFileSize(getAppContext(), size);
    }

    public static final Context getAppContext() {
        return sContext;
    }

    public static final int getMainThreadId() {
        return sThreadId;
    }

    public static final Handler getMainHandler() {
        return sHandler;
    }

    public static final Gson getGson() {
        return sGson;
    }

    public static final Resources getAppRes() {
        return getAppContext().getResources();
    }

    public static final String getPackageName() {
        return getAppContext().getPackageName();
    }

    public static final File getCacheDir() {
        return getAppContext().getCacheDir();
    }

    public static final String getString(int strId) {
        return getAppRes().getString(strId);
    }

    public static final String getString(int strId, String format) {
        return getAppRes().getString(strId, format);
    }

    public static final String[] getStrArr(int arrId) {
        return getAppRes().getStringArray(arrId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static final int getColor(int colId) {
        return getAppRes().getColor(colId, getAppContext().getTheme());
    }

    public static final void safeHandle(Runnable runnable) {
        int currId = Process.myTid();
        if (currId == getMainThreadId())
            runnable.run();
        else
            getMainHandler().post(runnable);
    }

    public static void RunApp(String packageName) {
        PackageInfo pi;
        try {
            pi = getAppContext().getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            // resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = getAppContext().getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                // intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                getAppContext().startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static File getCacheFile(String name) {
        return new File(getCacheDir(), name);
    }
}
