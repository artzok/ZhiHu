package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zok.art.zhihu.bean.ITitleBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public abstract class BasePageAdapter<T extends ITitleBean> extends PagerAdapter {
    private Context mContext;
    private List<View> views;
    protected List<T> data;

    public BasePageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // avoid null pointer exception and test failed of TextUtils.isEmpty() method
        return data == null ? " " : data.get(position).getTitle();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (views == null) return null; // maybe useless
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public T getItem(int position) {
        return data != null ? data.get(position) : null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * Just use to inflate layout.
     *
     * @return Context of a activity
     */
    protected Context getContext() {
        return mContext;
    }

    /**
     * Set data set and refresh UI.
     *
     * @param data Data set.
     */
    public void setDataAndRefresh(List<T> data) {
        this.data = data;
        views = new ArrayList<>(data.size());
        // builder data set of all item view
        for (int i = 0; i < data.size(); i++) {
            int itemLayoutId = getItemLayoutId();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View itemView = inflater.inflate(itemLayoutId, null);
            fillData(itemView, data.get(i));
            // add item titleView
            views.add(itemView);
        }
        notifyDataSetChanged();
    }

    /**
     * Child class must implement the method and return layout resource id of item view.
     *
     * @return Resource id of item view.
     */
    protected abstract int getItemLayoutId();

    /**
     * Child class must implement the method and fill UI.
     * @param itemView item view.
     * @param data data of item view.
     */
    protected abstract void fillData(View itemView, T data);


}
