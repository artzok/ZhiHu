package com.zok.art.zhihu.ui.sections;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.SectionListBean;
import com.zok.art.zhihu.ui.card.CardPresenter;
import com.zok.art.zhihu.utils.AppUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionsPresenter extends CardPresenter<SectionBean> {
    @Override
    public Observable<List<SectionBean>> getCardObservable(ApiService apiService) {
        return apiService.getSectionList().flatMap(
                new Func1<SectionListBean, Observable<List<SectionBean>>>() {
                    @Override
                    public Observable<List<SectionBean>> call(SectionListBean data) {
                        return Observable.just(data.getData());
                    }
                });
    }

    public String getTitle() {
        return AppUtil.getString(R.string.menu_sections_tips);
    }
}
