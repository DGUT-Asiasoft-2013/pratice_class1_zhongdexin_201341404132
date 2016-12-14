package com.example.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.helloworld.api.Service;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/8.
 */

public class NewContentActivity extends Activity {

    EditText editText, editTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_content);

        editText = (EditText) findViewById(R.id.note_text);
        editTitle = (EditText) findViewById(R.id.note_title);

        findViewById(R.id.newcontent_btnsend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendContext();
            }
        });

    }

    private void sendContext() {
        String text = editText.getText().toString();
        String title = editTitle.getText().toString();

        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("text", text)
                .addFormDataPart("title", title)
                .build();

        Request request = Service.requestBuilderWithApi("article")
                .post(body)
                .build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewContentActivity.this.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewContentActivity.this.onSucceed(responseBody);
                    }
                });
            }
        });
    }

    private void onSucceed(String responseBody) {
        new AlertDialog.Builder(this)
                .setMessage(responseBody)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        //第一个参数是第一个activity进入时的动画，第二个参数则是第二个activity退出时的动画。
                        overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);

                    }
                }).show();
    }

    private void onFailure(IOException e) {
        new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
    }


}
