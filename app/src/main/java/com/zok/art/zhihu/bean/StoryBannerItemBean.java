package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class StoryBannerItemBean extends BasicStoryBean {
    @SerializedName("image")
    private String mImageUrl;

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StoryBannerItemBean that = (StoryBannerItemBean) o;

        return mImageUrl != null ? mImageUrl.equals(that.mImageUrl) : that.mImageUrl == null;

    }

    @Override
    public String toString() {
        return "StoryBannerItemBean{" +
                super.toString() +
                "mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
