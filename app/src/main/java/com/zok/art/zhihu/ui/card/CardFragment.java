package com.zok.art.zhihu.ui.card;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.BaseRecyclerAdapter;
import com.zok.art.zhihu.adapter.CardAdapter;
import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.inter.ICardItem;
import com.zok.art.zhihu.ui.main.MainActivity;

import java.util.List;

import butterknife.BindView;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class CardFragment<T extends CardContract.Presenter> extends BaseFragment<T>
        implements CardContract.View, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.card_view)
    public RecyclerView mCardView;
    private CardAdapter mCardAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_card;
    }

    @Override
    protected void initialize() {
        // set fix height for performance
        mCardView.setHasFixedSize(true);

        // set stagger layout manager
        mCardView.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));

        // set adapter
        mCardAdapter = new CardAdapter();
        mCardView.setAdapter(mCardAdapter);

        // set item click listener
        mCardAdapter.setItemClickListener(this);

        // start
        mPresenter.start();
    }

    @Override
    public void onClick(View v, int position) {
        ICardItem item = (ICardItem) mCardAdapter.getItem(position);
        MainActivity activity = (MainActivity) getActivity();
        onItemClick(activity, item);
    }

    public abstract void onItemClick(MainActivity activity, ICardItem iCardItem);

    @Override
    public void updateCards(List<? extends ICardItem> data) {
        mCardAdapter.setDataAndRefresh(data);
    }
}
