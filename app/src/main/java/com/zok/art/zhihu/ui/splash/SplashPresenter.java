package com.zok.art.zhihu.ui.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.BingSplashBean;
import com.zok.art.zhihu.bean.SplashBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.BitmapCacheUtils;
import com.zok.art.zhihu.utils.NetWorkUtil;
import com.zok.art.zhihu.utils.SPUtil;
import com.zok.art.zhihu.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.realm.internal.android.JsonUtils;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.zok.art.zhihu.ui.splash.SplashContract.*;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SplashPresenter implements Presenter {
    private View mSplashView;
    private ApiService mService;
    private Subscription mUpdateSubscribe;
    private Subscription mLoadSubscribe;

    @Override
    public void attachView(View view) {
        mSplashView = view;
    }

    @Override
    public void start() {
        // init api service
        mService = ApiManager.getApiService();

        // get cache image author
        String author = SPUtil.getString(Constants.SPLASH_IMAGE_AUTHOR);

        // show splash image and author
        if (BitmapCacheUtils.checkCacheBitmap(Constants.SPLASH_IMAGE_NAME)) {
            Bitmap bitmap = BitmapCacheUtils.getCacheBitmap(Constants.SPLASH_IMAGE_NAME);
            mSplashView.showSplashImage(bitmap, author, 3000);
        } else {
            mSplashView.showSplashImage(getDefaultSplashBitmap(), author, 3000);
        }

        // update cache
        updateSplashCache();
    }

    @Override
    public void animationFinished() {
        if (mSplashView != null)
            mSplashView.goMainActivity();
    }

    @Override
    public void detachView() {
        mSplashView = null;
        if (mLoadSubscribe != null)
            mLoadSubscribe.unsubscribe();
        if (mUpdateSubscribe != null)
            mUpdateSubscribe.unsubscribe();
    }

    private void updateSplashCache() {
        if (!NetWorkUtil.isNetWorkAvailable(AppUtil.getAppContext())) return;
        int anInt = SPUtil.getInt(Constants.SPLASH_IMAGE_ORIGIN);
//        switch (anInt) {
//            case Constants.ZHIHU_SPLASH:
//                mUpdateSubscribe = loadSplash(mService.getSplashImage(),
//                        new ZhihuSplashAction());
//                break;
//            case Constants.BIND_SPLASH:
                mUpdateSubscribe = loadSplash(mService.getRaw(ApiService.BIND_SPLASH_URL),
                        new BingSplashAction());
//                break;
//        }
    }

    @SuppressWarnings("unchecked")
    private Subscription loadSplash(Observable observable, Action1 action1) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSplashView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }

    private class ZhihuSplashAction implements Action1<SplashBean> {
        @Override
        public void call(SplashBean splashBean) {
            // update splash author
            SPUtil.putString(Constants.SPLASH_IMAGE_AUTHOR, splashBean.getAuthor());
            // update bitmap
            loadNewestSplashBitmap(splashBean.getImageUrl());
        }
    }

    private class BingSplashAction implements Action1<ResponseBody> {
        @Override
        public void call(ResponseBody responseBody) {
            try {
                String string = responseBody.string();
                BingSplashBean bean = AppUtil.getGson().fromJson(string, BingSplashBean.class);
                List<BingSplashBean.ImagesBean> images = bean.getImages();
                if (images != null && images.size() > 0) {
                    // update splash author
                    BingSplashBean.ImagesBean imagesBean = images.get(0);
                    SPUtil.putString(Constants.SPLASH_IMAGE_AUTHOR, imagesBean.getCopyright());
                    // update bitmap
                    loadNewestSplashBitmap(imagesBean.getUrl());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadNewestSplashBitmap(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) return;

        // 避免没有必要的下载
        final String md5 = StringUtil.MD5(imageUrl);
        String cacheMd5 = SPUtil.getString(Constants.SPLASH_IMAGE_URL);
        if (!TextUtils.isEmpty(cacheMd5) && md5.equals(cacheMd5)) return;
        // 下载最新的Splash Image
        mLoadSubscribe = mService.getRaw(imageUrl).subscribeOn(Schedulers.io())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        InputStream is = responseBody.byteStream();
                        if (is != null && BitmapCacheUtils.cacheBitmap(is, Constants.SPLASH_IMAGE_NAME))
                            SPUtil.putString(Constants.SPLASH_IMAGE_URL, md5);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSplashView.showError("Download splash bitmap failed!", throwable);
                    }
                });
    }

    private Bitmap getDefaultSplashBitmap() {
        return BitmapFactory.decodeResource(
                AppUtil.getAppRes(), R.drawable.default_splash);
    }
}
