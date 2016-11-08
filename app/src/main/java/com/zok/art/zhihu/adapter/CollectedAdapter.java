package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.db.bean.ReadStateBean;
import com.zok.art.zhihu.utils.ImageLoaderManager;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class CollectedAdapter extends BaseListAdapter<ReadStateBean> {

    public CollectedAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int itemViewType) {
        return R.layout.refresh_item;
    }

    @Override
    protected BaseViewHolder createViewHolder(int itemViewType, View itemView) {
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_news_title)
        TextView mTitle;

        @BindView(R.id.iv_news_image)
        ImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(ReadStateBean data, int position) {
            // set title
            mTitle.setText(data.getTitle());
            // load image: Some items without pictures
            String url = data.getImageUrl();
            ImageLoaderManager.loadListItemImage(getContext(), mImage, url);
        }
    }
}
