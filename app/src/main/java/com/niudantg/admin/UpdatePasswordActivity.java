package com.niudantg.admin;

import niudantg.Application;
import niudantg.BaseActivity;
import niudantg.Consts;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ToastUtil;
import com.niudantg.util.Utils;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//注册界面
@ContentView(R.layout.layout_update_passward)
public class UpdatePasswordActivity extends BaseActivity implements CFHttpMsg {
	//
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	// 原密码
	@ViewInject(R.id.ed_oldpassword)
	private EditText ed_oldpassword;
	// 新密码
	@ViewInject(R.id.ed_newpassword1)
	private EditText ed_newpassword1;
	// 确认新密码
	@ViewInject(R.id.ed_newpassword2)
	private EditText ed_newpassword2;
	// 确认新密码
	@ViewInject(R.id.btn_up)
	private RelativeLayout btn_up;
	//
	private String oldpassword;
	private String newpassword1;
	private String newpassword2;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_up })
	public void onClick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;
		// 登陆按钮
		case R.id.btn_up:
			UpdatePassword();
			break;

		default:
			break;
		}
	}

	//
	private void UpdatePassword() {
		oldpassword = ed_oldpassword.getText().toString();
		newpassword1 = ed_newpassword1.getText().toString();
		newpassword2 = ed_newpassword2.getText().toString();

		oldpassword = Utils.setSrule(oldpassword);
		newpassword1 = Utils.setSrule(newpassword1);
		newpassword2 = Utils.setSrule(newpassword2);

		// if (oldpassword == null || oldpassword.equals("")) {
		// ToastUtil.showMessages(UpdatePasswordActivity.this, "原密码不能为空~");
		// return;
		// }
		if (newpassword1 == null || newpassword1.equals("")) {
			ToastUtil.showMessages(UpdatePasswordActivity.this, "新密码不能为空~");
			return;
		}
		if (newpassword1.length() < 6) {
			ToastUtil.showMessages(UpdatePasswordActivity.this, "新密码必须大于6位数字~");
			return;
		}
		if (newpassword2 == null || newpassword2.equals("")) {
			ToastUtil.showMessages(UpdatePasswordActivity.this, "确认新密码不能为空~");
			return;
		}
		if (!newpassword2.equals(newpassword1)) {
			ToastUtil.showMessages(UpdatePasswordActivity.this, "新密码确认不相同~");
			return;
		}
		mProgressDialog.setTitle("提交中");
		mProgressDialog.show();
		Message message = new Message();
		message.what = 9019;
		CFHttpClient.s().get(
				"?MsgType=9019&mobileType=android&Phone=" + Consts.user.Phone
						+ "&OldPassword=" + oldpassword + "&NewPassword="
						+ newpassword1, this, message, false);

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void httpMsg(Message m) {
		switch (m.what) {
		//
		case 9019:
			mProgressDialog.cancel();
			if (m.arg1 == 1) {
				ToastUtil.showMessages(UpdatePasswordActivity.this, "新密码设置成功~");
				Editor editor = Application.sharedpreferences.edit();
				editor.putBoolean("tg_login", false);
				editor.commit();
				Consts.tf_login = false;
				finish();
			} else if (m.arg1 == -2) {
				ToastUtil.showMessages(UpdatePasswordActivity.this,
						"原密码错误，请确认~");
			} else {
				ToastUtil
						.showMessages(UpdatePasswordActivity.this, "提交失败，请重试~");
			}
			break;

		default:
			break;
		}
	}

	public void onResume() {
		super.onResume();
	}

}
