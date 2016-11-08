package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.StoryListItemBean;
import com.zok.art.zhihu.utils.ImageLoaderManager;

import java.util.List;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class RefreshListAdapter extends BaseListAdapter<StoryListItemBean> {
    private static final int ITEM_TYPE_TITLE = 1;
    private static final int ITEM_TYPE_DATE = 2;
    private static final int ITEM_TYPE_COUNT = 3;

    public RefreshListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int itemViewType) {
        if (itemViewType == ITEM_TYPE_NORMAL)
            return R.layout.refresh_item;
        // title and date use a same layout
        return R.layout.refresh_item_decorate;
    }

    @Override
    protected BaseViewHolder createViewHolder(int itemViewType, View itemView) {
        if (itemViewType == ITEM_TYPE_NORMAL)
            return new NormalViewHolder(itemView);
        // title and date use a same view holder
        return new OtherViewHolder(itemView);
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        StoryListItemBean item = (StoryListItemBean) getItem(position);
        if (item.isTitle())
            return ITEM_TYPE_TITLE;
        if (item.isDate())
            return ITEM_TYPE_DATE;
        // return normal type
        return super.getItemViewType(position);
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
            ImageLoaderManager.loadListItemImage(getContext(), mImage, urls == null ? null : urls.get(0));

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
                    titleView.setText(R.string.refresh_item_title);
                    break;
                case ITEM_TYPE_DATE:
                    titleView.setText(data.getDateString());
                    break;
            }
        }
    }
}
