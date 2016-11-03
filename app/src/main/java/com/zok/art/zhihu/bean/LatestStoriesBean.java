package com.zok.art.zhihu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class LatestStoriesBean {
    @SerializedName("date")
    private Date mDate;
    @SerializedName("stories")
    private List<ListStoryBean> mListStories;
    @SerializedName("top_stories")
    private List<TopStoryBean> mTopStories;

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

    public List<TopStoryBean> getTopStories() {
        return mTopStories;
    }

    public void setTopStories(List<TopStoryBean> topStories) {
        mTopStories = topStories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LatestStoriesBean that = (LatestStoriesBean) o;

        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        if (mListStories != null ? !mListStories.equals(that.mListStories) : that.mListStories != null)
            return false;
        return mTopStories != null ? mTopStories.equals(that.mTopStories) : that.mTopStories == null;
    }
}
