package com.example.trubul.tumblrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";
    private SearchView mSearchView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activateBackButtonToolbar(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);  // provide access to the system search services
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        // Assumes current activity is the searchable activity
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());  // get a searchableInfo from searchable.xml
        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setIconified(false);  // expand the widget and show the keyboard

        // Listeners
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: called");

                // Share data Bundle-like
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());  // getApplicationContext() to retrieve data in another Activity
                sharedPreferences.edit().putString(SEARCH_USERNAME_KEY, query).apply();

                mSearchView.clearFocus();  // remove focus from the toolbar before returning to mainActivity (bug with physical keyboards)
                finish();  // and back to parent = mainActivity
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;  // default behaviour
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.clearFocus();
                finish();
                return false;
            }
        });

        return true;
    }
}