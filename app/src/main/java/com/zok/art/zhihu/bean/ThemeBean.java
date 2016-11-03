package com.zok.art.zhihu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemeBean implements Parcelable {
    private int color;
    private String thumbnail;
    private String description;
    private int id;
    private String name;

    public ThemeBean() {
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
        return "ThemeBean{" +
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

    protected ThemeBean(Parcel in) {
        this.color = in.readInt();
        this.thumbnail = in.readString();
        this.description = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ThemeBean> CREATOR = new Parcelable.Creator<ThemeBean>() {
        @Override
        public ThemeBean createFromParcel(Parcel source) {
            return new ThemeBean(source);
        }

        @Override
        public ThemeBean[] newArray(int size) {
            return new ThemeBean[size];
        }
    };
}
