package com.zok.art.zhihu.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zok.art.zhihu.R;

import static android.content.Context.WINDOW_SERVICE;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ToastUtil {
    private static Toast toast;


    public static void show(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void show(Context context, String msg, int iconResId) {
        TextView message = null;
        ImageView icon = null;
        Display display = null;

        final WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        /* create view and set content */
        final View notifyView = LayoutInflater.from(context).inflate(R.layout.toast_notify, null);

        Runnable sAction = new Runnable() {
            @Override
            public void run() {
                manager.removeView(notifyView);
            }
        };

        /* set msg  */
        message = (TextView) notifyView.findViewById(R.id.message);

        /* set icon */
        icon = (ImageView) notifyView.findViewById(R.id.icon);

        /* set layout parameter */
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;

        display = manager.getDefaultDisplay();
        params.x = (int) ((display.getWidth() - notifyView.getMeasuredWidth()) / 2.0f);
        params.y = (display.getHeight() - notifyView.getMeasuredHeight());

        // IMPORTANT1:
        // 如果希望默认的原点位于左上角，则必须设置
        params.gravity = Gravity.START | Gravity.TOP;

        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // set info
        message.setText(msg);
        icon.setImageResource(iconResId);

        /* add view to window manager */
        manager.addView(notifyView, params);

        notifyView.postDelayed(sAction, 3000);
    }
}
