package com.zok.art.zhihu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class NewsExtraBean implements Parcelable {
    private int comments;
    private int long_comments;
    private int popularity;
    private int short_comments;

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    @Override
    public String toString() {
        return "NewsExtraBean{" +
                "comments=" + comments +
                ", long_comments=" + long_comments +
                ", popularity=" + popularity +
                ", short_comments=" + short_comments +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.comments);
        dest.writeInt(this.long_comments);
        dest.writeInt(this.popularity);
        dest.writeInt(this.short_comments);
    }

    public NewsExtraBean() {
    }

    protected NewsExtraBean(Parcel in) {
        this.comments = in.readInt();
        this.long_comments = in.readInt();
        this.popularity = in.readInt();
        this.short_comments = in.readInt();
    }

    public static final Parcelable.Creator<NewsExtraBean> CREATOR = new Parcelable.Creator<NewsExtraBean>() {
        @Override
        public NewsExtraBean createFromParcel(Parcel source) {
            return new NewsExtraBean(source);
        }

        @Override
        public NewsExtraBean[] newArray(int size) {
            return new NewsExtraBean[size];
        }
    };
}
