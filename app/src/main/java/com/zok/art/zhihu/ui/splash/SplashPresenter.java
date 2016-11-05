package com.zok.art.zhihu.ui.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.SplashBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.BitmapCacheUtils;
import com.zok.art.zhihu.utils.NetWorkUtil;
import com.zok.art.zhihu.utils.SPUtil;
import com.zok.art.zhihu.utils.StringUtil;

import java.io.InputStream;

import okhttp3.ResponseBody;
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

    public SplashPresenter(){}

    public SplashPresenter(Intent intent) {
    }

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
        mUpdateSubscribe = mService.getSplash().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SplashBean>() {
                    @Override
                    public void call(SplashBean splashBean) {
                        // update splash author
                        SPUtil.putString(Constants.SPLASH_IMAGE_AUTHOR, splashBean.getAuthor());
                        // update bitmap
                        loadNewestSplashBitmap(splashBean.getImageUrl());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSplashView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });

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
