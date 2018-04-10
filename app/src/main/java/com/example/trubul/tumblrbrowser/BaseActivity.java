package com.example.trubul.tumblrbrowser;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by krzysiek
 * On 4/8/18.
 */

public class BaseActivity extends AppCompatActivity {

    static final String SEARCH_USERNAME_KEY = "SEARCH_USERNAME_KEY";
    static final String PHOTO_INTENT_KEY = "PHOTO_INTENT_KEY";
    static final String TEXT_INTENT_KEY = "TEXT_INTENT_KEY";
    static final String PHOTOTEXT_INTENT_KEY = "PHOTOTEXT_INTENT_KEY";


    void activateBackButtonToolbar(boolean isBackButton) {
        ActionBar actionBar = getSupportActionBar();

        if(actionBar == null) {
            Toolbar toolbar = findViewById(R.id.toolbar);  // 'inflate' the toolbar

            if(toolbar != null) {
                setSupportActionBar(toolbar);  // set the toolbar; setActionBar() requires API 21 hence support.v7
                actionBar = getSupportActionBar();  // get the toolbar
            }
        }

        // display the back-button toolbar or no
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isBackButton);
        }
    }
}
