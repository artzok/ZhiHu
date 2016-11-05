package com.zok.art.zhihu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemeItemBean implements Parcelable {
    private int color;
    private String thumbnail;
    private String description;
    private int id;
    private String name;

    public ThemeItemBean() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "ThemeItemBean{" +
                "color=" + color +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.color);
        dest.writeString(this.thumbnail);
        dest.writeString(this.description);
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected ThemeItemBean(Parcel in) {
        this.color = in.readInt();
        this.thumbnail = in.readString();
        this.description = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ThemeItemBean> CREATOR = new Parcelable.Creator<ThemeItemBean>() {
        @Override
        public ThemeItemBean createFromParcel(Parcel source) {
            return new ThemeItemBean(source);
        }

        @Override
        public ThemeItemBean[] newArray(int size) {
            return new ThemeItemBean[size];
        }
    };
}
