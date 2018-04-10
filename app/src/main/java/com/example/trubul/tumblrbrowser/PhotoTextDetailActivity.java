package com.example.trubul.tumblrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoTextDetailActivity extends BaseActivity {

    private static final String TAG = "PhotoTextDetailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_text_detail);
        activateBackButtonToolbar(true);

        Intent intent = getIntent();
        PostPhotoText singlePost = (PostPhotoText) intent.getSerializableExtra(PHOTOTEXT_INTENT_KEY);  // get data from TEXT_INTENT_KEY in intent 'dict'

        if(singlePost != null) {
            TextView photoTextTitle = findViewById(R.id.phototext_title);
            TextView photoTextDate = findViewById(R.id.phototext_date);
            ImageView photoTextImage = findViewById(R.id.phototext_image);
            TextView photoTextText = findViewById(R.id.phototext_text);

            Log.d(TAG, "XXXX" + singlePost.getDate());

            Resources res = getResources();

            // set TextViews with singlePost.xxx() on placeholders
//            photoTextTitle.setText(res.getString(R.string.title_text, singlePost.getTitle()));
            photoTextDate.setText(res.getString(R.string.date_text, singlePost.getDate()));

            String answerText = singlePost.getText();


            if (singlePost.getText().contains("<img src=")) {
                String link = null;

                Pattern patternImage = Pattern.compile("<img src=\"(.*?)\"");  // get link to image between HTML tags
                Pattern patternText = Pattern.compile("(?s)(.*)<figure(.*?)/figure>(.*)");  // ?s ignores newlines; then get text before or/and after image

                Matcher matcherImage = patternImage.matcher(answerText);
                Matcher matcherText = patternText.matcher(answerText);

                while (matcherImage.find()) {
                    link = matcherImage.group(1);
                }
                while (matcherText.find()) {
                    answerText = matcherText.group(1);
                    answerText += matcherText.group(3);
                }

                GlideApp.with(this)
                        .load(link)
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(photoTextImage);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoTextTitle.setText(Html.fromHtml(res.getString(R.string.title_text, singlePost.getTitle()), Html.FROM_HTML_MODE_COMPACT));
                photoTextText.setText(Html.fromHtml(answerText, Html.FROM_HTML_MODE_COMPACT));

            } else {
                photoTextTitle.setText(Html.fromHtml(res.getString(R.string.title_text, singlePost.getTitle())));
                photoTextText.setText(Html.fromHtml(answerText));
            }


        }
    }





}
