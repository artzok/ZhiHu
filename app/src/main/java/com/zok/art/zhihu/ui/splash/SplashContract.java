package com.zok.art.zhihu.ui.splash;

import android.graphics.Bitmap;

import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface SplashContract {
    interface View extends BaseView {
        void showSplashImage(Bitmap bitmap, String title, int duration);

        void goMainActivity();
    }

    interface Presenter extends BasePresenter<View> {
        void animationFinished();
    }
}
