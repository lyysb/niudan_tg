package com.niudantg.admin;

import niudantg.Application;
import niudantg.BaseActivity;
import niudantg.Consts;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.admin.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@ContentView(R.layout.layout_set)
public class SetActivity extends BaseActivity {
	//
	@ViewInject(R.id.btn_1)
	private LinearLayout btn_1;
	//
	@ViewInject(R.id.btn_2)
	private LinearLayout btn_2;

	//
	private Intent intent;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		intent = new Intent();

	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_1, R.id.btn_2 })
	public void viewonclick(View view) {
		switch (view.getId()) {
		//
		case R.id.btn_1:
			intent.setClass(SetActivity.this, UpdatePasswordActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_2:
			setQuerenDialog();
			break;
		default:
			break;
		}

	}

	// 确定界面
	protected void setQuerenDialog() {

		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		dlg.setCanceledOnTouchOutside(false);
		Window window = dlg.getWindow();

		window.setContentView(R.layout.dialog_queren);

		// title
		TextView t_title = (TextView) window.findViewById(R.id.text_title);
		t_title.setText("确定退出登录吗？");
		// 确定按钮
		RelativeLayout btn_dialog_ok = (RelativeLayout) window
				.findViewById(R.id.btn_dialog_ok);
		// 取消按钮
		RelativeLayout btn_dialog_cancel = (RelativeLayout) window
				.findViewById(R.id.btn_dialog_cancel);

		btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Consts.tf_login = false;
				Editor editor = Application.sharedpreferences.edit();
				editor.putBoolean("tg_login", false);
				editor.commit();
				dlg.cancel();
				finish();
			}
		});

		btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});

	}

	protected void onResume() {
		super.onResume();
		if (!Consts.tf_login) {
			finish();
		}
	}

}
