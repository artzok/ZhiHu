package com.zok.art.zhihu.ui.sections;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
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
public class SectionsPresenter implements SectionsContract.Presenter {

    private SectionsContract.View mView;
    private Subscription mSubscribe;

    @Override
    public void attachView(SectionsContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        RxJavaUtils.releaseSubscribe(mSubscribe);
        mView = null;
    }

    @Override
    public void start() {
        loadSections();
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
                        mView.updateSections(sections.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }
}
