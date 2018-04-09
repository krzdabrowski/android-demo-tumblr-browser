package com.example.trubul.tumblrbrowser;

import java.io.Serializable;

/**
 * Created by krzysiek
 * On 4/9/18.
 */

// to be "intentable"
class PostPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mType;
    private String mDate;
    private String mURL;
    private String mTitle;
    private String mSmallImage;
    private String mBigImage;

    public PostPhoto(String type, String date, String url, String title, String smallImage, String bigImage) {
        mType = type;
        mDate = date;
        mURL = url;
        mTitle = title;
        mSmallImage = smallImage;
        mBigImage = bigImage;
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

    String getSmallImage() {
        return mSmallImage;
    }

    String getBigImage() {
        return mBigImage;
    }

    @Override
    public String toString() {
        return "PostPhoto{" +
                "mType='" + mType + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mURL='" + mURL + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mSmallImage='" + mSmallImage + '\'' +
                ", mBigImage='" + mBigImage + '\'' +
                '}';
    }
}