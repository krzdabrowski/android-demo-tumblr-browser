package com.example.trubul.tumblrbrowser;

/**
 * Created by krzysiek
 * On 4/10/18.
 */

import java.io.Serializable;

// to be "intentable"
class PostOther implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mType;
    private String mDate;
    private String mURL;

    public PostOther(String type, String date, String url) {
        mType = type;
        mDate = date;
        mURL = url;
    }


    String getType() {
        return mType;
    }

    String getDate() {
        return mDate;
    }

    String getURL() { return mURL; }


    @Override
    public String toString() {
        return "PostOther{" +
                "mType='" + mType + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mURL='" + mURL + '\'' +
                '}';
    }
}