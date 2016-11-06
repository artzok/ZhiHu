package com.zok.art.zhihu.base;

import android.os.Bundle;

import com.zok.art.zhihu.ui.sections.SectionsContract;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface BaseFragmentContract {
    interface View extends BaseView {
        void stopUpdate();
        void reStartUpdate();
        Presenter getPresenter();
    }

    interface Presenter<T extends View> extends BasePresenter<T> {
        void stopUpdate();
        void reStartUpdate();
        void setParams(Bundle bundle);
    }
}
