package com.zok.art.zhihu.ui.sections;

import com.zok.art.zhihu.base.BaseFragmentContract;
import com.zok.art.zhihu.bean.SectionBean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface SectionsContract {
    interface View extends BaseFragmentContract.View {
        void updateSections(List<SectionBean> data);
    }
    interface Presenter extends BaseFragmentContract.Presenter<View> {
        void loadSections();
    }

}
