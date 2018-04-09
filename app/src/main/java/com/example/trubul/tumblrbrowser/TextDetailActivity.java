package com.example.trubul.tumblrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

public class TextDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detail);
        activateBackButtonToolbar(true);

        Intent intent = getIntent();
        PostText singlePost = (PostText) intent.getSerializableExtra(TEXT_INTENT_KEY);  // get data from TEXT_INTENT_KEY in intent 'dict'

        if(singlePost != null) {
            TextView textTitle = findViewById(R.id.text_title);
            TextView textDate = findViewById(R.id.text_date);
            TextView textData = findViewById(R.id.text_data);

            Resources res = getResources();

            // set TextViews with singlePost.xxx() on placeholders
            textDate.setText(res.getString(R.string.date_text, singlePost.getDate()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textTitle.setText(Html.fromHtml(res.getString(R.string.title_text, singlePost.getTitle()), Html.FROM_HTML_MODE_COMPACT));
                textData.setText(Html.fromHtml(singlePost.getText(), Html.FROM_HTML_MODE_COMPACT));

            } else {
                textTitle.setText(Html.fromHtml(res.getString(R.string.title_text, singlePost.getTitle())));
                textData.setText(Html.fromHtml(singlePost.getText()));
            }

        }
    }

}
