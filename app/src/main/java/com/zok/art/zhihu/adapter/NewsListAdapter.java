package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.StoryListItemBean;

import java.util.List;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class NewsListAdapter extends BaseListAdapter<StoryListItemBean> {
    private static final int ITEM_TYPE_TITLE = 1;
    private static final int ITEM_TYPE_DATE = 2;
    private int typeCount = 3;

    public NewsListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int itemViewType) {
        if (itemViewType == ITEM_TYPE_NORMAL)
            return R.layout.news_item;
        return R.layout.list_item_decord;
    }

    @Override
    protected BaseViewHolder createViewHolder(int itemViewType, View itemView) {
        if (itemViewType == ITEM_TYPE_NORMAL)
            return new NormalViewHolder(itemView);
        return new OtherViewHolder(itemView);
    }

    @Override
    public int getViewTypeCount() {
        return this.typeCount;
    }

    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }

    @Override
    public int getItemViewType(int position) {
        StoryListItemBean item = (StoryListItemBean) getItem(position);
        if (item.isTitle())
            return ITEM_TYPE_TITLE;
        if (item.isDate())
            return ITEM_TYPE_DATE;
        return ITEM_TYPE_NORMAL;
    }

    class NormalViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_news_title)
        TextView mTitle;

        @BindView(R.id.iv_news_image)
        ImageView mImage;

        NormalViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(StoryListItemBean data, int position) {
            // set title
            mTitle.setText(data.getTitle());

            // load image: Some items without pictures
            List<String> urls = data.getImageUrls();
            if (urls == null) {
                mImage.setVisibility(View.GONE);
            } else {
                mImage.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(urls.get(0)).into(mImage);
            }

            // set text color
            mTitle.setSelected(data.isRead());
        }
    }

    class OtherViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_text)
        TextView titleView;

        OtherViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(StoryListItemBean data, int position) {
            switch (getItemViewType(position)) {
                case ITEM_TYPE_TITLE:
                    titleView.setText("今日热点");
                    break;
                case ITEM_TYPE_DATE:
                    titleView.setText(data.getDateString());
                    break;
            }
        }
    }
}
