package com.zok.art.zhihu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zok.art.zhihu.di.component.DaggerFragmentComponent;
import com.zok.art.zhihu.di.component.FragmentComponent;
import com.zok.art.zhihu.di.module.FragmentModule;
import com.zok.art.zhihu.utils.LogUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class BaseFragment<T extends BaseFragmentContract.Presenter>
        extends Fragment implements BaseFragmentContract.View {
    // log utils
    protected final LogUtil log = LogUtil.getLogUtil(getClass());
    // p 层
    @Inject
    protected T mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        // 绑定视图对象
        ButterKnife.bind(this, view);
        // 注入P层，关联View层
        initInject();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    protected abstract void initialize();

    protected abstract int getLayoutResId();

    protected abstract void initInject();

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this, getArguments()))
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopUpdate();
    }

    @Override
    public void stopUpdate() {
        if (mPresenter != null)
            mPresenter.stopUpdate();
    }

    @Override
    public void reStartUpdate() {
        if (mPresenter != null)
            mPresenter.reStartUpdate();
    }

    @Override
    public void showError(String msg, Throwable e) {
        e.printStackTrace();
        log.d(msg + ":" + e.getMessage());
        View view = getView();
        if(view != null)
        Snackbar.make(view, msg, 2000).setAction("重试",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.start();
                    }
                }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle(mPresenter.getTitle());
    }

    @Override
    public void updateTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public T getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseApplication.sWatcher.watch(this);
    }
}
