package com.zok.art.zhihu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class NetWorkUtil {
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED)
            return true;
        return false;
    }
}
