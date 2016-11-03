package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class NewsDetailBean implements ITitleBean {

    @SerializedName("body")
    private String mHtmlBody;
    @SerializedName("image_source")
    private String mImageAuthor;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("image")
    private String mLagerImageUrl;
    @SerializedName("share_url")
    private String mShareUrl;
    @SerializedName("ga_prefix")
    private String mGaPrefix;
    @SerializedName("type")
    private int mType;
    @SerializedName("id")
    private long mId;
    @SerializedName("js")
    private List<String> mJavaScripts;
    @SerializedName("images")
    private List<String> mImages;
    @SerializedName("css")
    private List<String> mCss;

    public String getHtmlBody() {
        return mHtmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        mHtmlBody = htmlBody;
    }

    public String getImageAuthor() {
        return mImageAuthor;
    }

    public void setImageAuthor(String imageAuthor) {
        mImageAuthor = imageAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLagerImageUrl() {
        return mLagerImageUrl;
    }

    public void setLagerImageUrl(String lagerImageUrl) {
        mLagerImageUrl = lagerImageUrl;
    }

    public String getShareUrl() {
        return mShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        mShareUrl = shareUrl;
    }

    public String getGaPrefix() {
        return mGaPrefix;
    }

    public void setGaPrefix(String gaPrefix) {
        mGaPrefix = gaPrefix;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public List<String> getJavaScripts() {
        return mJavaScripts;
    }

    public void setJavaScripts(List<String> javaScripts) {
        mJavaScripts = javaScripts;
    }

    public List<String> getImages() {
        return mImages;
    }

    public void setImages(List<String> images) {
        mImages = images;
    }

    public List<String> getCss() {
        return mCss;
    }

    public void setCss(List<String> css) {
        mCss = css;
    }

    @Override
    public String toString() {
        return "NewsDetailBean{" +
                "mHtmlBody='" + mHtmlBody + '\'' +
                ", mImageAuthor='" + mImageAuthor + '\'' +
                ", mBigTitle='" + mTitle + '\'' +
                ", mLagerImageUrl='" + mLagerImageUrl + '\'' +
                ", mShareUrl='" + mShareUrl + '\'' +
                ", mGaPrefix='" + mGaPrefix + '\'' +
                ", mType=" + mType +
                ", mId=" + mId +
                ", mJavaScripts=" + mJavaScripts +
                ", mImages=" + mImages +
                ", mCss=" + mCss +
                '}';
    }
}
