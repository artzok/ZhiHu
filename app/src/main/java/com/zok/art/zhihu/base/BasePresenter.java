package com.zok.art.zhihu.base;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
    void start();
}
