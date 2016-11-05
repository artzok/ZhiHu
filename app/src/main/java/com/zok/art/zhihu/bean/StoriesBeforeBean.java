package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class StoriesBeforeBean {
    @SerializedName("date")
    private Date mDate;
    @SerializedName("stories")
    private List<StoryListItemBean> mListStories;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public List<StoryListItemBean> getListStories() {
        return mListStories;
    }

    public void setListStories(List<StoryListItemBean> listStories) {
        mListStories = listStories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoriesBeforeBean that = (StoriesBeforeBean) o;

        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        return mListStories != null ? mListStories.equals(that.mListStories) : that.mListStories == null;

    }

    @Override
    public String toString() {
        return "StoriesBeforeBean{" +
                "mDate=" + mDate +
                ", mListStories=" + mListStories +
                '}';
    }
}
