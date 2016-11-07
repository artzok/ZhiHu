package com.zok.art.zhihu.ui.themes;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.bean.ThemeListBean;
import com.zok.art.zhihu.ui.card.CardPresenter;
import com.zok.art.zhihu.utils.AppUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemesPresenter extends CardPresenter<ThemeItemBean> {
    @Override
    public Observable<List<ThemeItemBean>> getCardObservable(ApiService apiService) {
        return apiService.getLatestThemes().flatMap(
                new Func1<ThemeListBean, Observable<List<ThemeItemBean>>>() {
            @Override
            public Observable<List<ThemeItemBean>> call(ThemeListBean data) {
                return Observable.just(data.getOthers());
            }
        });
    }

    public String getTitle() {
       return AppUtil.getString(R.string.menu_themes_tips);
    }
}
