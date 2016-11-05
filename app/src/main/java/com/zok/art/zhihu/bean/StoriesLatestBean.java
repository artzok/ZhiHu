package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class StoriesLatestBean {
    @SerializedName("date")
    private Date mDate;
    @SerializedName("stories")
    private List<StoryListItemBean> mListStories;
    @SerializedName("top_stories")
    private List<StoryBannerItemBean> mBannerStories;

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

    public List<StoryBannerItemBean> getBannerStories() {
        return mBannerStories;
    }

    public void setBannerStories(List<StoryBannerItemBean> bannerStories) {
        mBannerStories = bannerStories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoriesLatestBean that = (StoriesLatestBean) o;

        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        if (mListStories != null ? !mListStories.equals(that.mListStories) : that.mListStories != null)
            return false;
        return mBannerStories != null ? mBannerStories.equals(that.mBannerStories) : that.mBannerStories == null;
    }
}
