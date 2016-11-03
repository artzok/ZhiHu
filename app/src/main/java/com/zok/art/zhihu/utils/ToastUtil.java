package com.zok.art.zhihu.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ToastUtil {
    public static Toast toast;

    public static void show(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
