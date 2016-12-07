package com.example.helloworld.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helloworld.R;
import com.example.helloworld.inputcells.SimpleTextInputCellFragment;

/**
 * Created by Administrator on 2016/12/6.
 */

public class PasswordRecoverStep1Fragment extends Fragment {

    SimpleTextInputCellFragment register_email ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_recover_step1, container);

        register_email = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.register_email);
        view.findViewById(R.id.step1_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        register_email.setLabelText("注册邮箱");
        register_email.setEditHint("请输入注册邮箱");
    }

    public static  interface OnGoNextListener{
        void onGoNext();
    }

    OnGoNextListener onGoNextListener ;

    public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
        this.onGoNextListener = onGoNextListener;
    }

    void goNext() {
        if (onGoNextListener == null) {
            onGoNextListener.onGoNext();
        }
    }
}
