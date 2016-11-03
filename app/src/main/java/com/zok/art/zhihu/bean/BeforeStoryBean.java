package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class BeforeStoryBean {
    @SerializedName("date")
    private Date mDate;
    @SerializedName("stories")
    private List<ListStoryBean> mListStories;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public List<ListStoryBean> getListStories() {
        return mListStories;
    }

    public void setListStories(List<ListStoryBean> listStories) {
        mListStories = listStories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeforeStoryBean that = (BeforeStoryBean) o;

        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        return mListStories != null ? mListStories.equals(that.mListStories) : that.mListStories == null;

    }

    @Override
    public String toString() {
        return "BeforeStoryBean{" +
                "mDate=" + mDate +
                ", mListStories=" + mListStories +
                '}';
    }
}
