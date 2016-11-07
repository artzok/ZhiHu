package com.zok.art.zhihu.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.ui.main.MainActivity;

import butterknife.BindView;

import static com.zok.art.zhihu.ui.splash.SplashContract.View;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SplashActivity extends
        BaseActivity<SplashContract.Presenter> implements View {

    @BindView(R.id.iv_splash_image)
    public ImageView mSplashImage;

    @BindView(R.id.tv_splash_author)
    public TextView mSplashAuthor;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void requestPermissionSucceed() {
        requestImmersion();     // 沉浸式模式
        mPresenter.start();     // 启动P层逻辑
    }

    @Override
    public void showSplashImage(Bitmap bitmap, String author, int duration) {
        mSplashImage.setImageBitmap(bitmap);
        mSplashAuthor.setText(author);
        mSplashImage.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        mSplashImage.animate().scaleX(1.2f).scaleY(1.2f).setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mPresenter.animationFinished();
                    }
                }).start();
    }

    @Override
    public void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.alpha_enter_anim, R.anim.alpha_exit_anim);
        finish();
    }
}
