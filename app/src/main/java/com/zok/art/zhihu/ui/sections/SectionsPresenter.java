package com.zok.art.zhihu.ui.sections;

import android.os.Bundle;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.base.BaseFragmentPresenter;
import com.zok.art.zhihu.bean.SectionListBean;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.RxJavaUtils;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionsPresenter extends BaseFragmentPresenter<SectionsContract.View>
        implements SectionsContract.Presenter {

    private Subscription mSubscribe;

    @Override
    public void start() {
        loadSections();
    }

    @Override
    public void stopUpdate() {
        RxJavaUtils.releaseSubscribe(mSubscribe);
    }

    @Override
    public void setParams(Bundle bundle) {
    }

    @Override
    public void loadSections() {
        mSubscribe = ApiManager.getApiService()
                .getSections().subscribeOn(Schedulers.io())
                .filter(new Func1<SectionListBean, Boolean>() {
                    @Override
                    public Boolean call(SectionListBean sections) {
                        return sections != null && sections.getData().size() > 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SectionListBean>() {
                    @Override
                    public void call(SectionListBean sections) {
                        if (mView != null)
                            mView.updateSections(sections.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mView != null)
                            mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }
}
