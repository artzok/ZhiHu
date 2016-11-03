package com.zok.art.zhihu.base;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface BaseView {
    /**
     * 当P层发生错误时调用
     */
    void showError(String msg, Throwable e);
}
