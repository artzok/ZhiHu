package com.zok.art.zhihu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class BaseStoryBean implements ITitleBean {
    @SerializedName("type")
    private int mType;
    @SerializedName("id")
    private long mId;
    @SerializedName("ga_prefix")
    private String mGaPrefix;
    @SerializedName("title")
    private String mTitle;

    @Expose(serialize = false, deserialize = false)
    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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

    public String getGaPrefix() {
        return mGaPrefix;
    }

    public void setGaPrefix(String gaPrefix) {
        mGaPrefix = gaPrefix;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseStoryBean that = (BaseStoryBean) o;

        if (mType != that.mType) return false;
        if (mId != that.mId) return false;
        if (mGaPrefix != null && !mGaPrefix.equals(that.mGaPrefix)) return false;
        return mTitle.equals(that.mTitle);
    }
}
