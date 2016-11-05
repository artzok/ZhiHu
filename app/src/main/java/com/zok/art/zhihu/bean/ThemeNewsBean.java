package com.zok.art.zhihu.bean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemeNewsBean {
    private String background;
    private int color;
    private String description;
    private String image;
    private String image_source;
    private String name;
    private List<EditorBean> editors;
    private List<StoryListItemBean> stories;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EditorBean> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorBean> editors) {
        this.editors = editors;
    }

    public List<StoryListItemBean> getStories() {
        return stories;
    }

    public void setStories(List<StoryListItemBean> stories) {
        this.stories = stories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThemeNewsBean that = (ThemeNewsBean) o;

        if (color != that.color) return false;
        if (background != null ? !background.equals(that.background) : that.background != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (image_source != null ? !image_source.equals(that.image_source) : that.image_source != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (editors != null ? !editors.equals(that.editors) : that.editors != null) return false;
        return stories != null ? stories.equals(that.stories) : that.stories == null;

    }
}
