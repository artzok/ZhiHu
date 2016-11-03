package com.zok.art.zhihu.bean;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public  class EditorsBean {
    private String avatar;
    private String bio;
    private int id;
    private String name;
    private String url;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EditorsBean that = (EditorsBean) o;

        if (id != that.id) return false;
        if (avatar != null ? !avatar.equals(that.avatar) : that.avatar != null) return false;
        if (bio != null ? !bio.equals(that.bio) : that.bio != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;

    }
}

