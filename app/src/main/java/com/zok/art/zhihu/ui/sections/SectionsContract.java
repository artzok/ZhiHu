package com.zok.art.zhihu.ui.sections;

import android.view.View;

import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;
import com.zok.art.zhihu.bean.SectionBean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface SectionsContract {
    interface View extends BaseView {
        void updateSections(List<SectionBean> data);

    }
    interface Presenter extends BasePresenter<View> {
        void loadSections();
    }

}
