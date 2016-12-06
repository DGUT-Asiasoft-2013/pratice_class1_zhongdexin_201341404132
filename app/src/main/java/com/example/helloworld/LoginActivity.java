package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.helloworld.inputcells.SimpleTextInputCellFragment;

public class LoginActivity extends Activity {

	SimpleTextInputCellFragment login_username;
	SimpleTextInputCellFragment login_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goRegister();
			}
		});

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gohello();
			}
		});

		findViewById(R.id.forgetpassword).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goRecoverRassword();
			}
		});

		login_username = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_username);
		login_password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
	}


	@Override
	protected void onResume() {
		super.onResume();

		login_username.setLabelText("账户名");
		login_username.setEditHint("请输入账户名");
		login_password.setLabelText("密码");
		login_password.setEditHint("请输入密码");
		login_password.setEditInputType(true);
	}

	private void gohello() {
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}

	private void goRecoverRassword() {
		Intent itnt = new Intent(this,PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}
