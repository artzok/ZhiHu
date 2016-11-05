package com.zok.art.zhihu.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.SectionBean;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionsAdapter extends BaseRecyclerAdapter<SectionBean> {

    @Override
    int getItemLayoutResId() {
        return R.layout.section_item;
    }

    @Override
    SectionViewHolder createViewHolder(View itemView, int type) {
        return new SectionViewHolder(itemView);
    }

    public class SectionViewHolder extends BaseRecyclerViewHolder<SectionBean> {
        @BindView(R.id.image_section)
        public ImageView icon;

        @BindView(R.id.title_section)
        public TextView title;

        @BindView(R.id.description_section)
        public TextView description;

        public SectionViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(SectionBean data) {
            Picasso.with(itemView.getContext()).load(data.getThumbnail()).into(icon);
            title.setText(data.getName());
            String text = data.getDescription();
            if (!TextUtils.isEmpty(text)) {
                description.setVisibility(View.VISIBLE);
                description.setText(text);
            } else {
                description.setVisibility(View.GONE);
            }
        }
    }
}
