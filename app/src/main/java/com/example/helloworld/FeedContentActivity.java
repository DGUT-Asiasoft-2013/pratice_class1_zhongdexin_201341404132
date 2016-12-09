package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedContentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_content);

        String text = getIntent().getStringExtra("text");

        TextView textview = (TextView)findViewById(R.id.feed_text);

        textview.setText(text);
    }
}
