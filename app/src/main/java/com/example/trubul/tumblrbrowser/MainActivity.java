package com.example.trubul.tumblrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements DataJSON.AdapterCallback,
                                                          GestureListener.OnRecyclerClickListener{
    private static final String TAG = "MainActivity";
    private RecyclerViewAdapter mAdapter;
    private int typeOfData;
    public static boolean flagInit = true;


    //////////////////////////////////////////  LIFECYCLE  //////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateBackButtonToolbar(false);

        // Create RecyclerView and set LayoutManager & OnItemTouchListener
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new GestureListener(this, recyclerView, this));

        // Create Adapter and set it to RecyclerView
        mAdapter = new RecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get data Bundle-like from SearchActivity
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = sharedPreferences.getString(SEARCH_USERNAME_KEY, "");

        if(username.length() > 0) {
            // Coming back from SearchActivity, it goes to onResume
            DataJSON json = new DataJSON(this);
            json.execute(username);
        }
    }


    ////////////////////////////////////////////  MENU  ////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /////////////////////////////////////////  INTERFACES  /////////////////////////////////////////
    @Override
    public void loadData(List<Object> data, DownloadStatus status) {
        if(status == DownloadStatus.OK) {
            Log.d(TAG, "loadData: DATA" + data);
            mAdapter.loadNewData(data);
        } else {
            Log.e(TAG, "loadData failed with status " + status);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = null;
        typeOfData = mAdapter.getItemViewType(position);  // get info if it's IMAGE (=0) or TEXT (=1) or PHOTOTEXT (=2) or OTHER (=3)

        if (typeOfData == 0) {
            intent = new Intent(this, PhotoDetailActivity.class);
            intent.putExtra(PHOTO_INTENT_KEY, mAdapter.getPhoto(position));  // share data Bundle-like
        } else if (typeOfData == 1) {
            intent = new Intent(this, TextDetailActivity.class);
            intent.putExtra(TEXT_INTENT_KEY, mAdapter.getText(position));
        } else if (typeOfData == 2) {
            intent = new Intent(this, PhotoTextDetailActivity.class);
            intent.putExtra(PHOTOTEXT_INTENT_KEY, mAdapter.getPhotoText(position));
        } else if (typeOfData == 3) {
            intent = new Intent(Intent.ACTION_VIEW);
            String postUrl = mAdapter.getOther(position).getURL();
            intent.setData(Uri.parse(postUrl));
    }

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String postUrl = null;

        typeOfData = mAdapter.getItemViewType(position);

        if (typeOfData == 0) {
            postUrl = mAdapter.getPhoto(position).getURL();
        } else if (typeOfData == 1) {
            postUrl = mAdapter.getText(position).getURL();
        } else if (typeOfData == 2) {
            postUrl = mAdapter.getPhotoText(position).getURL();
        } else if (typeOfData == 3) {
            postUrl = mAdapter.getOther(position).getURL();
        }

        intent.setData(Uri.parse(postUrl));
        startActivity(intent);
    }
}
