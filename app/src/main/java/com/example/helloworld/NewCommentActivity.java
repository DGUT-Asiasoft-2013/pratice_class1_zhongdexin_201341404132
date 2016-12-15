package com.example.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.helloworld.api.Service;
import com.example.helloworld.entity.Article;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/15.
 */

public class NewCommentActivity extends Activity {
    EditText editText;
    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);

        editText = (EditText) findViewById(R.id.new_comment_text);
        article = (Article) getIntent().getSerializableExtra("data");

        findViewById(R.id.new_comment_btnsent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendContent();
            }
        });
    }

    private void sendContent() {
        String text = editText.getText().toString();

        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("text",text).build();

        Request request = Service.requestBuilderWithApi("article/" + article.getId() + "/comments")
                .post(body).build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewCommentActivity.this.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responsebody = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewCommentActivity.this.onSucceed(responsebody);
                    }
                });
            }
        });
    }

    private void onSucceed(String responsebody) {
        new AlertDialog.Builder(this)
                .setMessage(responsebody)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(R.anim.none,R.anim.slide_out_bottom);
                    }
                }).show();
    }

    private void onFailure(IOException e) {
        new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
    }
}
