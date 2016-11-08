package com.zok.art.zhihu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.StoryBannerItemBean;
import com.zok.art.zhihu.utils.ImageLoaderManager;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class BannerPageAdapter extends BasePageAdapter<StoryBannerItemBean> {

    public BannerPageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.home_banner_item;
    }

    @Override
    protected void fillData(View itemView, StoryBannerItemBean bean) {
        // find view
        ImageView image = (ImageView) itemView.findViewById(R.id.iv_banner_image);
        TextView title = (TextView) itemView.findViewById(R.id.tv_banner_title);

        // set color filter
        image.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

        // set title
        title.setText(bean.getTitle());

        // load image and set title
        ImageLoaderManager.loadBannerItemImage(getContext(), image, bean.getImageUrl());
    }
}
