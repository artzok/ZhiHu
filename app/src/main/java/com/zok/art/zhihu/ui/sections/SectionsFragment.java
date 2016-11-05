package com.zok.art.zhihu.ui.sections;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ListView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.BaseRecyclerAdapter;
import com.zok.art.zhihu.adapter.NewsListAdapter;
import com.zok.art.zhihu.adapter.SectionsAdapter;
import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import rx.Subscription;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionsFragment extends BaseFragment<SectionsContract.Presenter>
        implements SectionsContract.View, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.sections_view)
    public RecyclerView mSectionsView;

    private SectionsAdapter mSectionsAdapter;
    private Subscription mSubscribe;



    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sections;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showError(String msg, Throwable e) {

    }

    @Override
    protected void initialize() {
        // set fix height for performance
        mSectionsView.setHasFixedSize(true);

        // set stagger layout manager
        mSectionsView.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));

        // set adapter
        mSectionsAdapter = new SectionsAdapter();
        mSectionsView.setAdapter(mSectionsAdapter);

        // set item click listener
        mSectionsAdapter.setItemClickListener(this);

        // start
        mPresenter.start();
    }

    @Override
    public void onClick(View v, int position) {
//        Intent intent = new Intent(getActivity(), SectionActivity.class);
        SectionBean item = mSectionsAdapter.getItem(position);
        MainActivity activity = (MainActivity) getActivity();
        activity.goSection(item);
//        intent.putExtra(Constants.EXTRA_INIT_PARAMS, item);
//        startActivity(intent);
    }

    @Override
    public void updateSections(List<SectionBean> data) {
        mSectionsAdapter.setDataAndRefresh(data);
    }

    public static SectionsFragment newInstance() {
        return new SectionsFragment();
    }
}
