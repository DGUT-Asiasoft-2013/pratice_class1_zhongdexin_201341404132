package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;

import com.example.helloworld.inputcells.SimpleTextInputCellFragment;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment loginname ;
	SimpleTextInputCellFragment password;
	SimpleTextInputCellFragment passwordrepeated;
	SimpleTextInputCellFragment email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		loginname = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment1);
		password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment3);
		passwordrepeated = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment4);
		email = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.fragment2);
	}

	@Override
	protected void onResume() {
		super.onResume();

		loginname.setLabelText("账户名");
		loginname.setEditHint("请输入账户名");

		email.setLabelText("电子邮箱");
		email.setEditHint("请输入电子邮箱");

		password.setLabelText("密码");
		password.setEditHint("请输入密码");
		password.setEditInputType(true);
		passwordrepeated.setLabelText("重复密码");
		passwordrepeated.setEditHint("请重复密码");
		passwordrepeated.setEditInputType(true);
	}
}
