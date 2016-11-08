package com.zok.art.zhihu.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static File sPicassoCacheFile;

    public static void initGlobal(Context appCtx) {
        sContext = appCtx;
        sGson = new Gson();
        sHandler = new Handler();
        sThreadId = Process.myTid();
        sCacheMap = new HashMap<>();
    }

    public static void putGlobal(String key, Object value) {
        sCacheMap.put(key, value);
    }

    public static Object getGlobal(String key, Object defaultValue) {
        Object va = sCacheMap.get(key);
        if (va == null) return defaultValue;
        return va;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return getAppRes().getDisplayMetrics();
    }

    public static String formatByteSize(long size) {
        return Formatter.formatFileSize(getAppContext(), size);
    }

    public static Context getAppContext() {
        return sContext;
    }

    public static int getMainThreadId() {
        return sThreadId;
    }

    public static Handler getMainHandler() {
        return sHandler;
    }

    public static Gson getGson() {
        return sGson;
    }

    public static Resources getAppRes() {
        return getAppContext().getResources();
    }

    public static String getPackageName() {
        return getAppContext().getPackageName();
    }

    public static File getCacheDir() {
        return getAppContext().getCacheDir();
    }

    public static String getString(int strId) {
        return getAppRes().getString(strId);
    }

    public static String getString(int strId, String format) {
        return getAppRes().getString(strId, format);
    }

    public static String[] getStrArr(int arrId) {
        return getAppRes().getStringArray(arrId);
    }

    public static int getColor(int colId) {
        return getAppRes().getColor(colId);
    }

    public static void safeHandle(Runnable runnable) {
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


    public static int getAppVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static File getCacheFile(String name) {
        return new File(getCacheDir(), name);
    }

    public static Intent createShareIntent(String shareTitle,
                                           String detailTitle,
                                           String msgText,
                                           File img) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (img == null || TextUtils.isEmpty(img.getAbsolutePath())) {
            intent.setType("text/plain");
        } else {
            Uri u = Uri.fromFile(img);
            intent.putExtra(Intent.EXTRA_STREAM, u);
            intent.setType("image/*");
        }
        Log.d("tag", detailTitle);
        intent.putExtra(Intent.EXTRA_SUBJECT, detailTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return Intent.createChooser(intent, shareTitle);
    }
}
