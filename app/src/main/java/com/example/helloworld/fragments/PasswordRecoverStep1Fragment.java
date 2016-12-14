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
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
//        inflate(int resource, ViewGroup root, boolean attachToRoot)三个参数
//        resource：需要加载布局文件的id，意思是需要将这个布局文件中加载到Activity中来操作。
//        root：需要附加到resource资源文件的根控件，就是inflate()会返回一个View对象
//              如果第三个参数attachToRoot为true，就将这个root作为根对象返回
//              否则仅仅将这个root对象的LayoutParams属性附加到resource对象的根布局对象上
//              也就是布局文件resource的最外层的View上，比如是一个LinearLayout或者其它的Layout对象。
//        attachToRoot：是否将root附加到布局文件的根视图上
            view = inflater.inflate(R.layout.fragment_password_recover_step1, null);

            register_email = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.register_email);
            view.findViewById(R.id.step1_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goNext();
                }
            });
        }


        return view;
    }

    public String getText() {
        return register_email.getText();
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
        if (onGoNextListener != null) {
            onGoNextListener.onGoNext();
        }
    }
}
