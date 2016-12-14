package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;

import com.example.helloworld.api.Service;
import com.example.helloworld.fragments.PasswordRecoverStep1Fragment;
import com.example.helloworld.fragments.PasswordRecoverStep2Fragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/6.
 */

public class PasswordRecoverActivity extends Activity {

    PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
    PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);

        //回调
        step1.setOnGoNextListener(new PasswordRecoverStep1Fragment.OnGoNextListener() {
            @Override
            public void onGoNext() {                //重写OnGoNextListener的onGoNext方法，传递进PasswordRecoverStep1Fragment中
                goStep2();
            }
        });

        step2.setOnSubmitClickListener(new PasswordRecoverStep2Fragment.OnSubmitClickListener() {
            @Override
            public void onSubmitClicked() {
                goSubmit();
            }
        });


        //用step1替换该布局中的container，step1中有button添加了onclick事件
        //onclick事件内执行goNext方法，goNext方法中包含onGoNext方法
        getFragmentManager().beginTransaction().replace(R.id.container,step1).commit();

    }

    private void goSubmit() {
        OkHttpClient client = Service.getShareClient();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("email",step1.getText())
                .addFormDataPart("passwordHash",MD5.getMD5(step2.getText()))
                .build();

        Request request = Service.requestBuilderWithApi("passwordrecover")
                .post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    void goStep2() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_out_right,
                        R.animator.slide_in_right,
                        R.animator.slide_out_left
                ).replace(R.id.container,step2)
                .addToBackStack(null)       //返回栈，按返回键返回step1界面,或者错误返回step1
                .commit();
    }
}
