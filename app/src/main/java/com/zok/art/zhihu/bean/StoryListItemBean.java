package com.zok.art.zhihu.bean;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class StoryListItemBean extends BasicStoryBean {

    @SerializedName("images")
    private List<String> mImageUrls;
    private String date;
    private String display_date;

    @Expose(serialize = false, deserialize = false)
    private boolean isDate;

    @Expose(serialize = false, deserialize = false)
    private String dateString;

    @Expose(serialize = false, deserialize = false)
    private boolean isTitle;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisplay_date() {
        return display_date;
    }

    public void setDisplay_date(String display_date) {
        this.display_date = display_date;
    }

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

        StoryListItemBean that = (StoryListItemBean) o;

        return mImageUrls != null ? mImageUrls.equals(that.mImageUrls) : that.mImageUrls == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeStringList(this.mImageUrls);
    }

    public StoryListItemBean() {
    }

    protected StoryListItemBean(Parcel in) {
        super(in);
        this.mImageUrls = in.createStringArrayList();
    }

    public static final Creator<StoryListItemBean> CREATOR = new Creator<StoryListItemBean>() {
        @Override
        public StoryListItemBean createFromParcel(Parcel source) {
            return new StoryListItemBean(source);
        }

        @Override
        public StoryListItemBean[] newArray(int size) {
            return new StoryListItemBean[size];
        }
    };
}
