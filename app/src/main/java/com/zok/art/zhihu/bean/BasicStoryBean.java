package com.zok.art.zhihu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class BasicStoryBean implements ITitleBean, Parcelable {
    private int type;
    private long id;
    private String ga_prefix;
    private String title;

    @Expose(serialize = false, deserialize = false)
    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicStoryBean that = (BasicStoryBean) o;

        if (type != that.type) return false;
        if (id != that.id) return false;
        if (ga_prefix != null && !ga_prefix.equals(that.ga_prefix)) return false;
        return title.equals(that.title);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeLong(this.id);
        dest.writeString(this.ga_prefix);
        dest.writeString(this.title);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
    }

     BasicStoryBean() {
    }

     BasicStoryBean(Parcel in) {
        this.type = in.readInt();
        this.id = in.readLong();
        this.ga_prefix = in.readString();
        this.title = in.readString();
        this.isRead = in.readByte() != 0;
    }

    public static final Parcelable.Creator<BasicStoryBean> CREATOR = new Parcelable.Creator<BasicStoryBean>() {
        @Override
        public BasicStoryBean createFromParcel(Parcel source) {
            return new BasicStoryBean(source);
        }

        @Override
        public BasicStoryBean[] newArray(int size) {
            return new BasicStoryBean[size];
        }
    };
}
