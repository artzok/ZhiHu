package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.StoryListItemBean;
import com.zok.art.zhihu.db.bean.ReadStateBean;

import java.util.List;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class CollectsAdapter extends BaseListAdapter<ReadStateBean> {

    public CollectsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int itemViewType) {
        return R.layout.news_item;
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
            if (TextUtils.isEmpty(url)) {
                mImage.setVisibility(View.GONE);
            } else {
                mImage.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(url).into(mImage);
            }
        }
    }
}
