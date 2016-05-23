package com.externie.EXViews;

/**
 * Created by externIE on 16/5/19.
 */
public class EXNavItem {
    private String mTitle;
    private String mDate;
    private String mTime;
    private String mAddress;
    private String mUrl;
    public EXNavItem(String title,String date,String time,String address,String url){
        mTitle = title;
        mDate = date;
        mTime = time;
        mAddress = address;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
