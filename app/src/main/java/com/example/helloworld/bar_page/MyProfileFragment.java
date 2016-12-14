package com.example.helloworld.bar_page;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.helloworld.R;
import com.example.helloworld.api.Service;
import com.example.helloworld.entity.User;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/8.
 */

public class MyProfileFragment extends Fragment {

    View view;

    TextView textView;
    ProgressBar progressBar;
    AvatarView avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_my_profile, null);

            textView = (TextView) view.findViewById(R.id.me_text);
            progressBar = (ProgressBar) view.findViewById(R.id.me_progress);
            avatar = (AvatarView) view.findViewById(R.id.av_avatar);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = Service.getShareClient();
        Request request = Service.requestBuilderWithApi("me")
                .method("GET",null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyProfileFragment.this.onFuiluer(call,e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {

                try {
                    //返回的数据封装在User类中
                    final User user = new ObjectMapper().readValue(response.body().bytes(), User.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyProfileFragment.this.onResponse(call, user);
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyProfileFragment.this.onFuiluer(call,e);
                        }
                    });
                }
            }
        });
    }

    protected void onResponse(Call arg0, User user) {
        progressBar.setVisibility(View.GONE);
        avatar.load(user);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.BLACK);
        textView.setText("Hello,"+user.getName());
    }

    protected void onFuiluer(Call call, Exception ex) {
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.RED);
        textView.setText(ex.getMessage());
    }
}
