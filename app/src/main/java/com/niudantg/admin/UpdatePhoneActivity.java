package com.niudantg.admin;

import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.UpdatePrice;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ToastUtil;
import com.niudantg.util.Utils;
import com.niudantg.admin.R;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//修改电话界面
@ContentView(R.layout.layout_update_phone)
public class UpdatePhoneActivity extends BaseActivity implements CFHttpMsg {
	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.t_name)
	private TextView t_name;
	//
	@ViewInject(R.id.t_old_phone)
	private TextView t_old_phone;
	//
	@ViewInject(R.id.ed_new_phone)
	private EditText ed_new_phone;
	// 登陆按钮
	@ViewInject(R.id.btn_up)
	private RelativeLayout btn_up;
	//
	private UpdatePrice updateprice;
	//
	private String Phone;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		if (Consts.customer.OpenBankName != null
				&& !Consts.customer.OpenBankName.equals("")) {
			t_name.setText(Consts.customer.OpenBankName);
		}
		if (Consts.customer.Phone != null && !Consts.customer.Phone.equals("")) {
			t_old_phone.setText(Consts.customer.Phone);
		}

	}

	// 注册协议
	long waitTime = 1500;
	long touchTime = 0;

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_up, R.id.btn_back })
	public void onClick(View v) {
		long currentTime = System.currentTimeMillis();
		switch (v.getId()) {

		//
		case R.id.btn_back:
			finish();
			break;
		//
		case R.id.btn_up:
			if (touchTime != 0) {
				if ((currentTime - touchTime) >= waitTime) {
					touchTime = currentTime;
					if (CheckNetworkState()) {
						setData();
					} else {
						ToastUtil.showMessages(UpdatePhoneActivity.this,
								"网络连接失败 ,请检打开网络!");
					}
				}
			} else {
				touchTime = currentTime;
				if (CheckNetworkState()) {
					setData();
				} else {
					ToastUtil.showMessages(UpdatePhoneActivity.this,
							"网络连接失败 ,请检打开网络!");
				}
			}
			break;

		default:
			break;
		}
	}

	private void setData() {
		Phone = ed_new_phone.getText().toString();
		Phone = Utils.setSrule(Phone);
		if (Phone == null || Phone.equals("")) {
			ToastUtil.showMessages(UpdatePhoneActivity.this, "手机号不能为空~");
			return;
		}
		if (Phone.length() != 11) {
			ToastUtil.showMessages(UpdatePhoneActivity.this, "必须是11位手机号~");
			return;
		}
		Message message = new Message();
		message.what = 9018;
		CFHttpClient.s().get(
				"?MsgType=9018&mobileType=android&CustomerId="
						+ Consts.customer.id + "&Phone=" + Phone, this,
				message, false);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void httpMsg(Message m) {
		switch (m.what) {
		//
		case 9018:
			mProgressDialog.cancel();
			if (m.arg1 == 1) {
				ToastUtil.showMessages(UpdatePhoneActivity.this, "设置成功~");
				finish();
			} else {
				ToastUtil.showMessages(UpdatePhoneActivity.this, "设置失败，请重试~");
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
