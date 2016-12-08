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

public class PasswordRecoverStep2Fragment extends Fragment {

    View view;
    SimpleTextInputCellFragment step2_PIN;
    SimpleTextInputCellFragment step2_newpassword;
    SimpleTextInputCellFragment Step2_newpasswordrepeated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_password_recover_step2, null);


            step2_PIN = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.step2_PIN);
            step2_newpassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.step2_newpassword);
            Step2_newpasswordrepeated = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.step2_newpasswordrepeated);

//        view.findViewById(R.id.step2_finish).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        step2_PIN.setLabelText("验证码");
        step2_PIN.setEditHint("请输入验证码");
        step2_newpassword.setLabelText("新密码");
        step2_newpassword.setEditHint("请输入新密码");
        step2_newpassword.setEditInputType(true);
        Step2_newpasswordrepeated.setLabelText("重复新密码");
        Step2_newpasswordrepeated.setEditHint("请重复新密码");
        Step2_newpasswordrepeated.setEditInputType(true);
    }
}
