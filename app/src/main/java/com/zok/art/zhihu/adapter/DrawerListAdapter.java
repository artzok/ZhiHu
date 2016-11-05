package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.ThemeItemBean;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class DrawerListAdapter extends BaseListAdapter<ThemeItemBean> {

    public DrawerListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int itemViewType) {
        return R.layout.left_drawer_item;
    }

    @Override
    protected BaseViewHolder createViewHolder(int itemViewType, View itemView) {
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_left_drawer_item_title)
        TextView themeTitle;

        private ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(ThemeItemBean data, int position) {
            themeTitle.setText(data.getName());
        }
    }
}
