package com.zok.art.zhihu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class SectionBean implements Parcelable {

    private String description;
    private long id;
    private String name;
    private String thumbnail;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.thumbnail);
    }

    public SectionBean() {
    }

    protected SectionBean(Parcel in) {
        this.description = in.readString();
        this.id = in.readLong();
        this.name = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<SectionBean> CREATOR = new Parcelable.Creator<SectionBean>() {
        @Override
        public SectionBean createFromParcel(Parcel source) {
            return new SectionBean(source);
        }

        @Override
        public SectionBean[] newArray(int size) {
            return new SectionBean[size];
        }
    };
}
