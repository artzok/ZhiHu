package com.zok.art.zhihu.ui.card;

import android.os.Bundle;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.base.BaseFragmentPresenter;
import com.zok.art.zhihu.inter.ICardItem;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.RxJavaUtils;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class CardPresenter<T extends ICardItem> extends BaseFragmentPresenter<CardContract.View>
        implements CardContract.Presenter {

    private Subscription mSubscribe;

    @Override
    public void start() {
        mView.updateTitle(getTitle());
        loadCards();
    }

    @Override
    public void stopUpdate() {
        RxJavaUtils.releaseSubscribe(mSubscribe);
    }

    @Override
    public void setParams(Bundle bundle) {
    }

    public abstract Observable<List<T>> getCardObservable(ApiService apiService);

    @Override
    public void loadCards() {
        Observable<List<T>> cardObservable =
                getCardObservable(ApiManager.getApiService());
        mSubscribe = cardObservable.subscribeOn(Schedulers.io())
                .filter(new Func1<List<? extends ICardItem>, Boolean>() {
                    @Override
                    public Boolean call(List<? extends ICardItem> iCardItems) {
                        return iCardItems != null && iCardItems.size() > 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<? extends ICardItem>>() {
                    @Override
                    public void call(List<? extends ICardItem> iCardItems) {
                        mView.updateCards(iCardItems);
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
