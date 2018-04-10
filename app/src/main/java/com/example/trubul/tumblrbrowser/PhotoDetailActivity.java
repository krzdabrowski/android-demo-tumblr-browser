package com.example.trubul.tumblrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateBackButtonToolbar(true);

        Intent intent = getIntent();
        PostPhoto singlePost = (PostPhoto) intent.getSerializableExtra(PHOTO_INTENT_KEY);  // get data from PHOTO_INTENT_KEY in intent 'dict'

        if(singlePost != null) {
            TextView date = findViewById(R.id.photo_date);
            TextView title = findViewById(R.id.photo_title);
            ImageView image = findViewById(R.id.photo_image);

            Resources res = getResources();

            // set TextViews with singlePost.xxx() on placeholders
            date.setText(res.getString(R.string.date_text, singlePost.getDate()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title.setText(Html.fromHtml(res.getString(R.string.title_text, singlePost.getTitle()), Html.FROM_HTML_MODE_COMPACT));
            } else {
                title.setText(Html.fromHtml(res.getString(R.string.title_text, singlePost.getTitle())));
            }

            GlideApp.with(this)
                    .load(singlePost.getBigImage())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        }
    }

}