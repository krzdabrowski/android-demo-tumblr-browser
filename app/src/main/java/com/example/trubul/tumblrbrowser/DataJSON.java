package com.example.trubul.tumblrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysiek
 * On 4/8/18.
 */


// aka <type of Params sent to the task upon execution, Progress (bar), type of Result of the background operation>
class DataJSON extends AsyncTask<String, Void, List<Object>> implements DataRaw.JSONCallback {

    private static final String TAG = "DataJSON";
    private final AdapterCallback mCallback;
    private List<Object> mPostList = null;


    interface AdapterCallback {
        void loadData(List<Object> data, DownloadStatus status);
    }

    public DataJSON(AdapterCallback callback) {
        mCallback = callback;
    }


    private String createUri(String username) {
        Uri.Builder builder = new Uri.Builder();

        return builder.scheme("https")
                .authority(username + ".tumblr.com")
                .appendPath("api")
                .appendPath("read")
                .appendPath("json")
                .appendQueryParameter("debug", "1")
                .build().toString();
    }

    @Override
    public void parseJSON(String data, DownloadStatus status) {
        if(status == DownloadStatus.OK) {
            mPostList = new ArrayList<>();

            try{
                JSONObject jsonData = new JSONObject(data); // python-like {key: value} of all elements
                JSONArray itemsArray = jsonData.getJSONArray("posts");

                for(int i=0; i<itemsArray.length(); i++) { // for each elements (json/dict)
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);

                    // Type
                    String type = jsonPhoto.getString("type");

                    // Date
                    String date = jsonPhoto.getString("date");

                    // URL
                    String postUrl = jsonPhoto.getString("url");

                    // Title/source and data
                    String title;
                    String smallImage;
                    String bigImage;
                    String text;

                    if (type.equals("photo")) {
                        if (jsonPhoto.getString("photo-caption").equals("")) {
                            title = null;
                        } else {
                            title = jsonPhoto.getString("photo-caption");
                        }
                        smallImage = jsonPhoto.getString("photo-url-400");
                        bigImage = jsonPhoto.getString("photo-url-1280");
                        PostPhoto singlePhoto = new PostPhoto(type, date, postUrl, title, smallImage, bigImage);
                        mPostList.add(singlePhoto);

                    } else if (type.equals("regular")) {
                        title = jsonPhoto.getString("regular-title");
                        text = jsonPhoto.getString("regular-body");
                        PostText singleText = new PostText(type, date, postUrl, title, text);
                        mPostList.add(singleText);

                    } else if (type.equals("conversation")) {
                        title = jsonPhoto.getString("conversation-title");
                        text = jsonPhoto.getString("conversation-text");
                        PostText singleText = new PostText(type, date, postUrl, title, text);
                        mPostList.add(singleText);

                    } else if (type.equals("quote")) {
                        title = jsonPhoto.getString("quote-source");
                        text = jsonPhoto.getString("quote-text");
                        PostText singleText = new PostText(type, date, postUrl, title, text);
                        mPostList.add(singleText);

                    } else if (type.equals("answer")) {
                        title = jsonPhoto.getString("question");
                        text = jsonPhoto.getString("answer");
                        PostText singleText = new PostText(type, date, postUrl, title, text);
                        mPostList.add(singleText);
                    }

                }
            } catch(JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "parseJSON: Error processing JSON data " + e.getMessage());
                status = DownloadStatus.FAILED;
            }
        }

    }

    @Override
    protected List<Object> doInBackground(String... strings) {
        String myUri = createUri(strings[0]);
        Log.d(TAG, "uri is: " + myUri);
        DataRaw raw = new DataRaw(this);

        String data = raw.downloadData(myUri);
        raw.parseJSON(data);  // after parsing fill PhotoList with PostPhoto objects

        return mPostList;
    }

    @Override
    protected void onPostExecute(List<Object> posts) {
        if(mCallback != null) {
            mCallback.loadData(mPostList, DownloadStatus.OK);
        }

    }

}