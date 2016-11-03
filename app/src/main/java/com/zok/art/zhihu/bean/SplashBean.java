package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SplashBean {
    @SerializedName("text")
    private String mAuthor;
    @SerializedName("img")
    private String mImageUrl;

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "SplashBean{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
