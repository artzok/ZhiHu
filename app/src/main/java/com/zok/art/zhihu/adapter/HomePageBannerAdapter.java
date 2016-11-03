package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.TopStoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class HomePageBannerAdapter extends BasePageAdapter<TopStoryBean> {

    public HomePageBannerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.home_page_banner_item;
    }

    @Override
    protected void fillData(View itemView, TopStoryBean bean) {
        // find view
        ImageView image = (ImageView) itemView.findViewById(R.id.iv_banner_image);
        TextView title = (TextView) itemView.findViewById(R.id.tv_banner_title);

        // set color filter
        image.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

        // set title
        title.setText(bean.getTitle());

        // load image and set title
        Picasso.with(getContext()).load(bean.getImageUrl()).into(image);
    }
}
