package com.zok.art.zhihu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public abstract class BaseFragment<T extends BasePresenter>
        extends Fragment implements BaseView {
    // log utils
    protected final LogUtil log = LogUtil.getLogUtil(getClass());
    // p 层
    @Inject
    protected T mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
}
