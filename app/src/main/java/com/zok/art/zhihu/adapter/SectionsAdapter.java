package com.zok.art.zhihu.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.inter.ICardItem;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionsAdapter<T extends ICardItem> extends BaseRecyclerAdapter<T> {

    @Override
    int getItemLayoutResId() {
        return R.layout.section_item;
    }

    @Override
    SectionViewHolder createViewHolder(View itemView, int type) {
        return new SectionViewHolder(itemView);
    }

    public class SectionViewHolder extends BaseRecyclerViewHolder<T> {
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
        public void fillData(T data) {
            Log.d("tag", "fillData: "+data.getThumbnail());
            String thumbnail = data.getThumbnail();
            if (!TextUtils.isEmpty(thumbnail))
                Picasso.with(itemView.getContext()).load(thumbnail).into(icon);
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
