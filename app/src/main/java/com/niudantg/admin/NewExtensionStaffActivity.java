package com.niudantg.admin;

import java.net.URLEncoder;

import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.ExtensionStaff;
import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ToastUtil;
import com.niudantg.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@ContentView(R.layout.layout_new_extensionstaff)
public class NewExtensionStaffActivity extends BaseActivity implements
		CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.ed_name)
	private EditText ed_name;
	//
	@ViewInject(R.id.ed_phone)
	private EditText ed_phone;
	//
	@ViewInject(R.id.ed_passward)
	private EditText ed_passward;
	//
	@ViewInject(R.id.btn_type1)
	private Button btn_type1;
	//
	@ViewInject(R.id.btn_type2)
	private Button btn_type2;
	//
	@ViewInject(R.id.btn_type3)
	private Button btn_type3;
	//
	@ViewInject(R.id.btn_bd)
	private Button btn_bd;
	//
	@ViewInject(R.id.btn_boss)
	private Button btn_boss;
	//
	@ViewInject(R.id.btn_status)
	private Button btn_status;
	//
	@ViewInject(R.id.btn_region)
	private RelativeLayout btn_region;
	//
	@ViewInject(R.id.t_region)
	private TextView t_region;
	//
	@ViewInject(R.id.btn_up)
	private RelativeLayout btn_up;

	//
	private int EId = 0;
	private String Name;
	private String Phone;
	private String Password;
	private int BD_Status = 0;
	private String CityName;
	private int CityId;
	private int Level = 0;
	private int Type = 2;
	private String Remark;
	private int Status;
	//
	private ExtensionStaff ef;
	//
	private Intent intent;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		intent = new Intent();
		Consts.newef_tf = false;
		EId = getIntent().getIntExtra("EId", 0);
		Name = getIntent().getStringExtra("Name");
		Phone = getIntent().getStringExtra("Phone");
		Password = getIntent().getStringExtra("Password");
		Level = getIntent().getIntExtra("Level", 0);
		BD_Status = getIntent().getIntExtra("BD_Status", 0);
		CityName = getIntent().getStringExtra("CityName");
		CityId = getIntent().getIntExtra("CityId", 0);
		Type = getIntent().getIntExtra("Type", 0);
		Status = getIntent().getIntExtra("Status", 0);

		if (Name != null && !Name.equals("")) {
			ed_name.setText(Name);
		}
		if (Phone != null && !Phone.equals("")) {
			ed_phone.setText(Phone);
		}
		if (Password != null && !Password.equals("")) {
			ed_passward.setText(Password);
		}
		if (CityName != null && !CityName.equals("")) {
			t_region.setText(CityName);
		}
		if (BD_Status == 0) {
			btn_bd.setText("关");
			btn_bd.setBackgroundColor(this.getResources()
					.getColor(R.color.gray));
		} else {
			btn_bd.setText("开");
			btn_bd.setBackgroundResource(R.drawable.btn_login);
		}
		if (Level == 0) {
			btn_boss.setText("关");
			btn_boss.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
		} else {
			btn_boss.setText("开");
			btn_boss.setBackgroundResource(R.drawable.btn_login);
		}
		if (Status == 2) {
			btn_status.setText("离职");
			btn_status.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
		} else if (Status == 0) {
			btn_status.setText("在职");
			btn_status.setBackgroundResource(R.drawable.btn_login);
		}
		// 类型1,公司员工，2运营商团队，4运营商主管
		switch (Type) {
		//
		case 0:
			Remark = "未设置";
			Type = 0;
			btn_type1.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type2.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type3.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			break;
		//
		case 1:
			Remark = "公司员工";
			Type = 1;
			btn_type1.setBackgroundResource(R.drawable.btn_login);
			btn_type2.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type3.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			break;
		//
		case 2:
			Remark = "运营商员工";
			Type = 2;
			btn_type1.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type2.setBackgroundResource(R.drawable.btn_login);
			btn_type3.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			break;
		//
		case 3:
			Remark = "运营商主管";
			Type = 3;
			btn_type1.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type2.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type3.setBackgroundResource(R.drawable.btn_login);
			break;

		default:
			break;
		}

	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_up, R.id.btn_region, R.id.btn_type1,
			R.id.btn_type2, R.id.btn_type3, R.id.btn_bd, R.id.btn_boss,
			R.id.btn_status })
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
		case R.id.btn_region:
			intent.setClass(NewExtensionStaffActivity.this,
					ProvinceListActivity.class);
			// 0显示全国选择城，1不显示全国选择县区，2不显示全国选择城市
			intent.putExtra("Type", 2);
			startActivity(intent);
			break;
		//
		case R.id.btn_type1:
			Remark = "公司员工";
			Type = 1;
			btn_type1.setBackgroundResource(R.drawable.btn_login);
			btn_type2.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type3.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			break;
		//
		case R.id.btn_type2:
			Remark = "运营商员工";
			Type = 2;
			btn_type1.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type2.setBackgroundResource(R.drawable.btn_login);
			btn_type3.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			break;
		//
		case R.id.btn_type3:
			Remark = "运营商主管";
			Type = 3;
			btn_type1.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type2.setBackgroundColor(this.getResources().getColor(
					R.color.gray));
			btn_type3.setBackgroundResource(R.drawable.btn_login);
			break;
		//
		case R.id.btn_bd:
			if (BD_Status == 1) {
				BD_Status = 0;
				btn_bd.setText("关");
				btn_bd.setBackgroundColor(this.getResources().getColor(
						R.color.gray));
			} else {
				BD_Status = 1;
				btn_bd.setText("开");
				btn_bd.setBackgroundResource(R.drawable.btn_login);
			}
			break;
		//
		case R.id.btn_boss:
			if (Level == 1) {
				Level = 0;
				btn_boss.setText("关");
				btn_boss.setBackgroundColor(this.getResources().getColor(
						R.color.gray));
			} else {
				Level = 1;
				btn_boss.setText("开");
				btn_boss.setBackgroundResource(R.drawable.btn_login);
			}
			break;
		//
		case R.id.btn_status:
			if (Status == 0) {
				Status = 2;
				btn_status.setText("离职");
				btn_status.setBackgroundColor(this.getResources().getColor(
						R.color.gray));
			} else {
				Status = 0;
				btn_status.setText("在职");
				btn_status.setBackgroundResource(R.drawable.btn_login);
			}
			break;
		default:
			break;
		}
	}

	private void setData() {

		Name = ed_name.getText().toString();
		Phone = ed_phone.getText().toString();
		Password = ed_passward.getText().toString();
		//
		Name = Utils.setSrule(Name);
		Phone = Utils.setSrule(Phone);
		Password = Utils.setSrule(Password);
		if (Name == null || Name.equals("")) {
			ToastUtil
					.showMessages(NewExtensionStaffActivity.this, "推广员名称不能为空~");
			return;
		}
		if (Phone == null || Phone.equals("")) {
			ToastUtil.showMessages(NewExtensionStaffActivity.this, "电话不能为空~");
			return;
		}
		if (Password == null || Password.equals("")) {
			ToastUtil.showMessages(NewExtensionStaffActivity.this, "密码不能为空~");
			return;
		}
		if (CityId == 0) {
			ToastUtil.showMessages(NewExtensionStaffActivity.this, "城市信息不能为空~");
			return;
		}
		if (ef == null) {
			ef = new ExtensionStaff();
		}
		ef.id = EId;
		ef.Name = Name;
		ef.Phone = Phone;
		ef.Password = Password;
		ef.Level = Level;
		ef.BD_Status = BD_Status;
		ef.Type = Type;
		ef.CityId = CityId;
		ef.CityName = CityName;
		ef.Remark = Remark;
		ef.Status = Status;
		ef.Type1 = 1;

		setNewEF();
	}

	// 添加推广员
	private void setNewEF() {
		mProgressDialog.show();
		mProgressDialog.setTitle("提交中...");
		String str = URLEncoder.encode(JSON.toJSONString(ef));
		Message m = new Message();
		m.what = 9025;
		CFHttpClient.s().get(
				"?MsgType=9025&mobileType=android&extensionstaff=" + str
						+ "&EId=" + Consts.user.id, this, m, true);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1) {
			ToastUtil.showMessages(NewExtensionStaffActivity.this, "增加推广员成功~");
			Consts.newef_tf = true;
			finish();
		} else if (m.arg1 == -2) {
			ToastUtil.showMessages(NewExtensionStaffActivity.this,
					"此手机号已注册，不能重复。");
		} else if (m.arg1 == 2) {
			ToastUtil.showMessages(NewExtensionStaffActivity.this, "推广员信息修改成功");
			Consts.newef_tf = true;
			finish();
		} else {
			ToastUtil.showMessages(NewExtensionStaffActivity.this, "增加失败,请重试~");
		}
	}

	protected void onResume() {
		super.onResume();
		if (Consts.region_tf) {
			Consts.region_tf = false;
			CityName = Consts.Cityinfo.Name;
			CityId = Consts.Cityinfo.CityId;
			if (CityName != null && !CityName.equals("")) {
				t_region.setText(CityName);
			}
		}
	}
}
