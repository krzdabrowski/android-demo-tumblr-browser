package com.example.trubul.tumblrbrowser;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by krzysiek
 * On 4/8/18.
 */

enum DownloadStatus { IDLE, DOWNLOADING, OK, FAILED }
// IDLE = not downloading data now
// DOWNLOADING = downloading data now
// OK = downloading was successful
// FAILED = invalid url, downloading problem, empty data etc


class DataRaw {

    private static final String TAG = "DataRaw";
    private DownloadStatus mDownloadStatus;
    private final JSONCallback mCallback;


    interface JSONCallback {
        void parseJSON(String data, DownloadStatus status);
    }

    public DataRaw(JSONCallback callback) {
        mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }


    String downloadData(String uri) {
        HttpURLConnection connection = null;
        BufferedReader in = null;
        String inputLine;

        if (uri == null) {
            mDownloadStatus = DownloadStatus.FAILED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.DOWNLOADING;

            URL myUrl = new URL(uri);
            connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();


            // Create a new buffered reader and String Builder
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            // Check if the line we are reading is not null
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "getRawData: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "getRawData: IO Exception reading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "getRawData: Security exception. Needs permission? " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "getRawData: Error closing stream " + e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED;
        return null;
    }

    void parseJSON(String result) {
        if(mCallback != null) {
            mCallback.parseJSON(result, mDownloadStatus);
        }
    }

}