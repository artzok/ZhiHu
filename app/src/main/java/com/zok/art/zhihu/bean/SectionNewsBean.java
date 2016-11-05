package com.zok.art.zhihu.bean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionNewsBean {
    private String name;
    private int timestamp;
    private List<StoryListItemBean> stories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<StoryListItemBean> getStories() {
        return stories;
    }

    public void setStories(List<StoryListItemBean> stories) {
        this.stories = stories;
    }
}
