package com.example.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.helloworld.api.Service;
import com.example.helloworld.entity.User;
import com.example.helloworld.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {

	SimpleTextInputCellFragment login_account;
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
				goRecoverPassword();
			}
		});

		login_account = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_username);
		login_password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
	}


	@Override
	protected void onResume() {
		super.onResume();

		login_account.setLabelText("账户名");
		login_account.setEditHint("请输入账户名");
		login_password.setLabelText("密码");
		login_password.setEditHint("请输入密码");
		login_password.setEditInputType(true);
	}

	void gohello() {
//		Intent itnt = new Intent(this,HelloWorldActivity.class);
//		startActivity(itnt);

//		OkHttpClient client = new OkHttpClient.Builder().build();

		OkHttpClient client = Service.getShareClient();

		MultipartBody requestBody = new MultipartBody.Builder()
				.addFormDataPart("account",login_account.getText())
				.addFormDataPart("passwordHash",MD5.getMD5(login_password.getText()))
				.build();

//		Request request = new Request.Builder()
//				.url("http://172.27.0.51:8080/membercenter/api/login")
		Request request = Service.requestBuilderWithApi("login")
				.method("POST",null)
				.post(requestBody)
				.build();

		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setCancelable(false);
		dlg.setMessage("正在登录");
		dlg.show();
		dlg.setCanceledOnTouchOutside(false);

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, final IOException e) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dlg.dismiss();
						Toast.makeText(LoginActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				//从服务器接受到数据
				final String responseString = response.body().string();
				ObjectMapper mapper = new ObjectMapper();
				//将数据存储在User类中
				final User user = mapper.readValue(responseString, User.class);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dlg.dismiss();
						new AlertDialog.Builder(LoginActivity.this)
								.setMessage("Hello," + user.getName())
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										Intent itnt =new Intent(LoginActivity.this,HelloWorldActivity.class);
										startActivity(itnt);
									}
								}).show();

					}
				});
			}
		});


	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}

	void goRecoverPassword() {
		Intent itnt = new Intent(this,PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}
