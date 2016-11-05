package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.StoryListItemBean;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionNewsAdapter extends BaseListAdapter<StoryListItemBean> {
    public SectionNewsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int itemViewType) {
        return R.layout.section_news;
    }

    @Override
    protected BaseViewHolder createViewHolder(int itemViewType, View itemView) {
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_news)
        public TextView tvNews;
        @BindView(R.id.iv_news)
        public ImageView ivNews;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(StoryListItemBean data, int position) {
            tvNews.setText(data.getTitle());
            if (data.getImageUrls() != null) {
                Picasso.with(itemView.getContext()).load(data.getImageUrls().get(0)).into(ivNews);
            }
        }
    }
}
