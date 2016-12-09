package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/12/8.
 */

public class NewContentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_content);

        findViewById(R.id.newcontent_btnsend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //第一个参数是第一个activity进入时的动画，第二个参数则是第二个activity退出时的动画。
                overridePendingTransition(R.anim.none,R.anim.slide_out_bottom);
            }
        });

    }
}
