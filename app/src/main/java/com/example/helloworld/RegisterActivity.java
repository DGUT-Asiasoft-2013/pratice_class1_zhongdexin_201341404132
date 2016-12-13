package com.example.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.helloworld.inputcells.PictureInputCellFragment;
import com.example.helloworld.inputcells.SimpleTextInputCellFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment fragaccount ;
	SimpleTextInputCellFragment fragpassword;
	SimpleTextInputCellFragment fragpasswordrepeated;
	SimpleTextInputCellFragment fragemail;
	SimpleTextInputCellFragment fragidname;

	PictureInputCellFragment fragAvatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		fragaccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment1);
		fragpassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment4);
		fragidname = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment2);
		fragpasswordrepeated = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment5);
		fragemail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.fragment3);

		fragAvatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_head);

		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submit();
			}
		});

	}

	private void submit() {
		String password = fragpassword.getText();
		String passwordRepeat = fragpasswordrepeated.getText();

		if (!password.equals(passwordRepeat)) {

			new AlertDialog.Builder(RegisterActivity.this)
					.setTitle("输入密码错误")
					.setMessage("重复密码不一致")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setNegativeButton("好",null)
					.show();

			return;
		}

		password = MD5.getMD5(password);

		String account = fragaccount.getText();
		String name = fragidname.getText();
		String email = fragemail.getText();

		OkHttpClient client = new OkHttpClient.Builder().build();

		//请求数据对象
		MultipartBody.Builder requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account",account)
				.addFormDataPart("name",name)
				.addFormDataPart("email",email)
				.addFormDataPart("passwordHash",password);

		if (fragAvatar.getPngData() != null) {

			//第一个参数为数据封装名
			//第二个参数为图片名
			//第三个参数为图片
			requestBody
					.addFormDataPart("avatar", "avatar",
							RequestBody.create(MediaType.parse("image/png"),
									fragAvatar.getPngData()));
		}


		Request request = new Request.Builder()
				.url("http://172.27.0.51:8080/membercenter/api/register")
				.method("POST",null)
				.post(requestBody.build())
				.build();

		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("请稍等");
		//点屏幕和物理返回键退出进度对话框
		progressDialog.setCancelable(false);
		progressDialog.show();
		//物理返回键可以退出进度框，点屏幕无效
		progressDialog.setCanceledOnTouchOutside(false);

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(final Call call, final IOException e) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressDialog.dismiss();

						RegisterActivity.this.onFailure(call,e);
					}
				});
			}

			@Override
			public void onResponse(final Call call, final Response response) throws IOException {

				//这个函数必须在后台线程中调用
				final String responseString = response.body().string();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressDialog.dismiss();

						try {
							RegisterActivity.this.onResponse(call, responseString);
						} catch (Exception e) {
							e.printStackTrace();
							RegisterActivity.this.onFailure(call,e);
						}
					}
				});
			}
		});

	}

	void onResponse(Call arg0, String responseBady) {
		new AlertDialog.Builder(this)
				.setTitle("注册成功")
				.setMessage(responseBady)
				.setPositiveButton("好", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
	}

	void onFailure(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
				.setTitle("请求失败")
				.setMessage(arg1.getLocalizedMessage().toString())
				.setNegativeButton("好",null).show();
	}

	@Override
	protected void onResume() {
		super.onResume();

		fragaccount.setLabelText("账户名");
		fragaccount.setEditHint("请输入账户名");

		fragidname.setLabelText("昵称");
		fragidname.setEditHint("请输入昵称");

		fragemail.setLabelText("电子邮箱");
		fragemail.setEditHint("请输入电子邮箱");

		fragpassword.setLabelText("密码");
		fragpassword.setEditHint("请输入密码");
		fragpassword.setEditInputType(true);

		fragpasswordrepeated.setLabelText("重复密码");
		fragpasswordrepeated.setEditHint("请重复密码");
		fragpasswordrepeated.setEditInputType(true);
	}
}
