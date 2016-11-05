package com.zok.art.zhihu.ui.sections;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.SectionsAdapter;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.SectionListBean;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.RxJavaUtils;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SectionsActivity extends BaseActivity {
    @BindView(R.id.sections_view)
    public RecyclerView mSectionsView;

    private SectionsAdapter mSectionsAdapter;
    private Subscription mSubscribe;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sections;
    }

    @Override
    protected void setWindowFeature() {
    }

    @Override
    protected void initInject() {
    }

    @Override
    public void showError(String msg, Throwable e) {
    }

    @Override
    protected void requestPermissionSucceed() {
        initSections();
        loadSections();
    }

    @Override
    protected void onDestroy() {
        RxJavaUtils.releaseSubscribe(mSubscribe);
        super.onDestroy();
    }

    private void initSections() {
        mSectionsView.setHasFixedSize(true);
        mSectionsView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mSectionsAdapter = new SectionsAdapter();
        mSectionsView.setAdapter(mSectionsAdapter);
    }

    public void updateSections(List<SectionBean> data) {
        mSectionsAdapter.setDataAndRefresh(data);
    }

    private void loadSections() {
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
                        updateSections(sections.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showError(AppUtil.getString(R.string.load_failed), throwable);
                    }
                });
    }
}
