package com.example.trubul.tumblrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoTextDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_text_detail);
        activateBackButtonToolbar(true);

        Intent intent = getIntent();
        PostPhotoText singlePost = (PostPhotoText) intent.getSerializableExtra(PHOTOTEXT_INTENT_KEY);  // get data from TEXT_INTENT_KEY in intent 'dict'

        if(singlePost != null) {
            TextView date = findViewById(R.id.phototext_date);
            TextView title = findViewById(R.id.phototext_title);
            ImageView image = findViewById(R.id.phototext_image);
            TextView data = findViewById(R.id.phototext_data);

            Resources res = getResources();
            String answerText = singlePost.getText();

            // set TextViews with singlePost.xxx() on placeholders
            date.setText(res.getString(R.string.date_text, singlePost.getDate()));

            if (answerText.contains("<img src=")) {
                String imageLink = null;

                Pattern patternImage = Pattern.compile("<img src=\"(.*?)\"");  // get imageLink between HTML <img src> tags
                Pattern patternText = Pattern.compile("(?s)(.*)<figure(.*?)/figure>(.*)");  // ?s ignores newlines; then get data before or/and after image imageLink

                Matcher matcherImage = patternImage.matcher(answerText);
                Matcher matcherText = patternText.matcher(answerText);

                while (matcherImage.find()) {
                    imageLink = matcherImage.group(1);
                }
                while (matcherText.find()) {
                    answerText = matcherText.group(1);
                    answerText += matcherText.group(3);
                }

                GlideApp.with(this)
                        .load(imageLink)
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(image);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title.setText(Html.fromHtml(res.getString(R.string.question_text, singlePost.getTitle()), Html.FROM_HTML_MODE_COMPACT));
                data.setText(Html.fromHtml(answerText, Html.FROM_HTML_MODE_COMPACT));

            } else {
                title.setText(Html.fromHtml(res.getString(R.string.question_text, singlePost.getTitle())));
                data.setText(Html.fromHtml(answerText));
            }
        }
    }

}
