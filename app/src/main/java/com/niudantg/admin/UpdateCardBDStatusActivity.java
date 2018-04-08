package com.niudantg.admin;

import java.net.URLEncoder;
import ktx.pojo.domain.EquipmentInfo;
import niudantg.BaseActivity;
import niudantg.Consts;
import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ToastUtil;
import com.niudantg.util.Utils;
import com.niudantg.admin.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.layout_card_bd)
public class UpdateCardBDStatusActivity extends BaseActivity implements
		CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;

	//
	@ViewInject(R.id.t_code)
	private TextView t_code;
	//
	@ViewInject(R.id.t_status)
	private TextView t_status;
	//
	@ViewInject(R.id.t_deviceid)
	private TextView t_deviceid;
	//
	@ViewInject(R.id.ed_deviceid)
	private EditText ed_deviceid;
	//
	@ViewInject(R.id.t_machineid)
	private TextView t_machineid;
	//
	@ViewInject(R.id.t_machineid_title)
	private TextView t_machineid_title;
	//
	@ViewInject(R.id.btn_1)
	private Button btn_1;
	//
	@ViewInject(R.id.btn_2)
	private Button btn_2;
	//
	@ViewInject(R.id.btn_3)
	private Button btn_3;
	//
	@ViewInject(R.id.btn_4)
	private Button btn_4;
	//
	@ViewInject(R.id.btn_5)
	private Button btn_5;
	//
	@ViewInject(R.id.btn_6)
	private Button btn_6;
	//
	@ViewInject(R.id.btn_7)
	private Button btn_7;
	//
	@ViewInject(R.id.btn_8)
	private Button btn_8;

	//
	@ViewInject(R.id.lay_1)
	private LinearLayout lay_1;
	//
	@ViewInject(R.id.lay_2)
	private LinearLayout lay_2;
	//
	@ViewInject(R.id.t_up)
	private TextView t_up;

	//
	private int ID;
	private String Code;
	private String DeviceID;
	private int MachineID = 0;
	//
	private int Type;
	//
	private EquipmentInfo eqinfo;
	//
	private Intent intent;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		intent = new Intent();
		ID = getIntent().getIntExtra("ID", 0);
		Code = getIntent().getStringExtra("Code");
		DeviceID = getIntent().getStringExtra("DeviceID");
		MachineID = getIntent().getIntExtra("MachineID", 0);

		if (Code != null && !Code.equals("")) {
			t_code.setText(String.format("扭蛋机编号：%s", Code));
		}

		if (DeviceID != null && !DeviceID.equals("") && !DeviceID.equals("0")) {
			lay_1.setVisibility(View.GONE);
			lay_2.setVisibility(View.GONE);
			ed_deviceid.setVisibility(View.GONE);
			t_up.setText("解绑");
			Type = 1;
			t_status.setText("绑定状态：已绑定");
			t_status.setTextColor(this.getResources().getColor(R.color.red));
			if (DeviceID != null && !DeviceID.equals("")) {
				t_deviceid.setText(String.format("支付模块ID：%s", DeviceID));
			}
			t_machineid_title.setText(String.format("扭蛋机组合编号：%d", MachineID));
			t_machineid.setVisibility(View.GONE);

		} else {
			lay_1.setVisibility(View.VISIBLE);
			lay_2.setVisibility(View.VISIBLE);
			ed_deviceid.setVisibility(View.VISIBLE);
			t_up.setText("绑定");
			Type = 0;
			t_status.setText("绑定状态：未绑定");
			t_status.setTextColor(this.getResources().getColor(R.color.green1));
			MachineID = Integer.valueOf(Code.substring(6, 7));
			if (MachineID > 0 && MachineID < 9) {
				t_machineid.setText(String.format("%d", MachineID));
			}

			t_machineid.setVisibility(View.VISIBLE);
		}
	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_up, R.id.btn_1, R.id.btn_2, R.id.btn_3,
			R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8 })
	public void viewonclick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;
		//
		case R.id.btn_up:
			setData();
			break;
		//
		case R.id.btn_1:
			MachineID = 1;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		//
		case R.id.btn_2:
			MachineID = 2;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		//
		case R.id.btn_3:
			MachineID = 3;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		//
		case R.id.btn_4:
			MachineID = 4;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		//
		case R.id.btn_5:
			MachineID = 5;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		//
		case R.id.btn_6:
			MachineID = 6;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		//
		case R.id.btn_7:
			MachineID = 7;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		//
		case R.id.btn_8:
			MachineID = 8;
			t_machineid.setText(String.format("%d", MachineID));
			break;
		default:
			break;
		}
	}

	private void setData() {
		if (Type == 0) {
			DeviceID = ed_deviceid.getText().toString();
			//
			DeviceID = Utils.setSrule(DeviceID);
			if (DeviceID == null || DeviceID.equals("")) {
				ToastUtil.showMessages(UpdateCardBDStatusActivity.this,
						"支付模块编号不能为空~");
				return;
			}
			if (DeviceID.length() != 5) {
				ToastUtil.showMessages(UpdateCardBDStatusActivity.this,
						"支付模块编号必须是5位~");
				return;
			}
			if (MachineID == 0) {
				ToastUtil.showMessages(UpdateCardBDStatusActivity.this,
						"扭蛋机组合编号未设置~");
				return;
			}
		}

		if (eqinfo == null) {
			eqinfo = new EquipmentInfo();
		}
		eqinfo.id = ID;
		eqinfo.DeviceID = DeviceID;
		eqinfo.MachineID = MachineID;

		eqinfo.Code = Code;

		setAddCard();
	}

	// 添加设备
	private void setAddCard() {
		mProgressDialog.show();
		mProgressDialog.setTitle("提交中...");
		String str = URLEncoder.encode(JSON.toJSONString(eqinfo));
		Message m = new Message();
		m.what = 9015;
		CFHttpClient.s().get(
				"?MsgType=9015&mobileType=android&einfo=" + str + "&EId="
						+ Consts.user.id + "&Type=" + Type, this, m, true);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1) {
			if (Type == 0) {
				ToastUtil
						.showMessages(UpdateCardBDStatusActivity.this, "绑定成功~");
			} else {
				ToastUtil
						.showMessages(UpdateCardBDStatusActivity.this, "解绑成功~");
			}
			finish();
		} else {
			ToastUtil
					.showMessages(UpdateCardBDStatusActivity.this, "请求失败,请重试~");
		}
	}

	protected void onResume() {
		super.onResume();
	}
}
