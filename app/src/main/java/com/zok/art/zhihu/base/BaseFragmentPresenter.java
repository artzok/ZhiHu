package com.zok.art.zhihu.base;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class BaseFragmentPresenter<T extends BaseFragmentContract.View>
        implements BaseFragmentContract.Presenter<T> {
    protected T mView;
    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        stopUpdate();
        mView = null;
    }

    @Override
    public void reStartUpdate() {
        start();
    }
}
