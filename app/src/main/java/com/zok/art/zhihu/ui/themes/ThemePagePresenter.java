package com.zok.art.zhihu.ui.themes;

import android.os.Bundle;

import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.ListStoryBean;
import com.zok.art.zhihu.bean.ThemeBean;
import com.zok.art.zhihu.bean.ThemeNewsBean;
import com.zok.art.zhihu.ui.refresh.RefreshPresenter;

import java.util.List;

import rx.Observable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemePagePresenter extends
        RefreshPresenter<ThemeNewsBean, ThemePageContract.View, ThemeBean>
    implements ThemePageContract.Presenter {

    public ThemePagePresenter(Bundle initParams) {
        super(initParams);
    }

    @Override
    protected Observable<ThemeNewsBean>
    getLatestObservable(ApiService apiService, ThemeBean params) {
        return apiService.getTheTheme(params.getId());
    }

    @Override
    public List<ListStoryBean> getListBeans(ThemeNewsBean mode) {
        return mode.getStories();
    }

    @Override
    public String getTitle() {
        return mLatestData.getName();
    }
}
