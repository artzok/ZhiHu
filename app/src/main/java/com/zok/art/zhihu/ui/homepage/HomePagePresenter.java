package com.zok.art.zhihu.ui.homepage;

import android.os.Bundle;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.LatestStoriesBean;
import com.zok.art.zhihu.bean.ListStoryBean;
import com.zok.art.zhihu.ui.refresh.RefreshPresenter;
import com.zok.art.zhihu.utils.AppUtil;

import java.util.List;

import rx.Observable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class HomePagePresenter extends RefreshPresenter<LatestStoriesBean, HomePageContract.View, Object>
        implements HomePageContract.Presenter {

    public HomePagePresenter(Bundle initParams) {
        super(initParams);
    }

    @Override
    protected Observable<LatestStoriesBean> getLatestObservable(ApiService apiService, Object params) {
        return apiService.latestNewsStories();
    }

    @Override
    public List<ListStoryBean> getListBeans(LatestStoriesBean mode) {
        return mode.getListStories();
    }

    @Override
    public String getTitle() {
        return AppUtil.getString(R.string.menu_home_tip);
    }
}
