package com.zok.art.zhihu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class CommentItemBean implements Parcelable {
    private String author;
    private String content;
    private String avatar;
    private int time;
    private int id;
    private int likes;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.avatar);
        dest.writeInt(this.time);
        dest.writeInt(this.id);
        dest.writeInt(this.likes);
    }

    public CommentItemBean() {
    }

    protected CommentItemBean(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.avatar = in.readString();
        this.time = in.readInt();
        this.id = in.readInt();
        this.likes = in.readInt();
    }

    public static final Parcelable.Creator<CommentItemBean> CREATOR = new Parcelable.Creator<CommentItemBean>() {
        @Override
        public CommentItemBean createFromParcel(Parcel source) {
            return new CommentItemBean(source);
        }

        @Override
        public CommentItemBean[] newArray(int size) {
            return new CommentItemBean[size];
        }
    };
}
