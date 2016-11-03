package com.zok.art.zhihu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ListStoryBean extends BaseStoryBean {

    @SerializedName("images")
    private List<String> mImageUrls;

    @Expose(serialize = false, deserialize = false)
    private boolean isDate;

    @Expose(serialize = false, deserialize = false)
    private String dateString;

    @Expose(serialize = false, deserialize = false)
    private boolean isTitle;

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setDate(boolean date) {
        isDate = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public List<String> getImageUrls() {
        return mImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        mImageUrls = imageUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ListStoryBean that = (ListStoryBean) o;

        return mImageUrls != null ? mImageUrls.equals(that.mImageUrls) : that.mImageUrls == null;

    }
}
