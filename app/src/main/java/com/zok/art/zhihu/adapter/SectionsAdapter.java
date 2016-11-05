package com.zok.art.zhihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.SectionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder> {

    private List<SectionBean> data;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fillData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setDataAndRefresh(List<SectionBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_section)
        public ImageView icon;
        @BindView(R.id.title_section)
        public TextView title;
        @BindView(R.id.description_section)
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

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
