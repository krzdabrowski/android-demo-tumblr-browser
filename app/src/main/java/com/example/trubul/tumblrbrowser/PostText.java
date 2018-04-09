package com.example.trubul.tumblrbrowser;

import java.io.Serializable;

/**
 * Created by krzysiek
 * On 4/9/18.
 */

// to be "intentable"
class PostText implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mType;
    private String mDate;
    private String mURL;
    private String mTitle;
    private String mText;

    public PostText(String type, String date, String url, String title, String text) {
        mType = type;
        mDate = date;
        mURL = url;
        mTitle = title;
        mText = text;
    }


    String getType() {
        return mType;
    }

    String getDate() {
        return mDate;
    }

    String getURL() { return mURL; }

    String getTitle() {
        return mTitle;
    }

    String getText() {
        return mText;
    }

    @Override
    public String toString() {
        return "PostText{" +
                "mType='" + mType + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mURL='" + mURL + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mText='" + mText + '\'' +
                '}';
    }
}