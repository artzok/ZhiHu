package com.zok.art.zhihu.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class VersionBean {
    @SerializedName("status")
    private int mStatusCode;
    @SerializedName("msg")
    private String mUpdateInfo;
    @SerializedName("url")
    private String mDownloadUrl;
    @SerializedName("latest")
    private String mLastVersion;

    public int getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(int statusCode) {
        mStatusCode = statusCode;
    }

    public String getMDownloadUrl() {
        return mDownloadUrl;
    }

    public void setMDownloadUrl(String mDownloadUrl) {
        this.mDownloadUrl = mDownloadUrl;
    }

    public String getLastVersion() {
        return mLastVersion;
    }

    public void setLastVersion(String lastVersion) {
        mLastVersion = lastVersion;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "VersionBean{" +
                "mStatusCode=" + mStatusCode +
                ", mUpdateInfo='" + mUpdateInfo + '\'' +
                ", mDownloadUrl='" + mDownloadUrl + '\'' +
                ", mLastVersion='" + mLastVersion + '\'' +
                '}';
    }
}
