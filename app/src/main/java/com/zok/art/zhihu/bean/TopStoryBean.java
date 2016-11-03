package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class TopStoryBean extends BaseStoryBean {
    @SerializedName("image")
    private String mImageUrl;

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "TopStoryBean{" +
                super.toString() +
                "mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
