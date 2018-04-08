package com.niudantg.admin;

import java.net.URLEncoder;

import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.Customer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

@ContentView(R.layout.layout_set_customerinfo2)
public class SetCustomerInfoActivity2 extends BaseActivity implements CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;

	//
	@ViewInject(R.id.ed_name)
	private EditText ed_name;
	//
	@ViewInject(R.id.ed_people)
	private EditText ed_people;
	//
	@ViewInject(R.id.ed_phone)
	private EditText ed_phone;
	//
	@ViewInject(R.id.btn_region)
	private RelativeLayout btn_region;
	//
	@ViewInject(R.id.t_region)
	private TextView t_region;
	//
	@ViewInject(R.id.ed_address)
	private EditText ed_address;

	//
	@ViewInject(R.id.btn_up)
	private RelativeLayout btn_up;
	//
	@ViewInject(R.id.btn_to_up2)
	private TextView btn_to_up2;
	//
	@ViewInject(R.id.t_up)
	private TextView t_up;

	//
	@ViewInject(R.id.ed_RentCash)
	private EditText ed_RentCash;
	//
	@ViewInject(R.id.t_type1)
	private TextView t_type1;
	//
	@ViewInject(R.id.btn_nei)
	private Button btn_nei;
	//
	@ViewInject(R.id.btn_wai)
	private Button btn_wai;

	//
	@ViewInject(R.id.ed_remark)
	private EditText ed_remark;

	//
	@ViewInject(R.id.t_typename)
	private TextView t_typename;
	//
	@ViewInject(R.id.btn_type)
	private RelativeLayout btn_type;
	//
	@ViewInject(R.id.ed_carinfo)
	private EditText ed_carinfo;
	//
	private Customer customer = new Customer();
	//
	private Intent intent;
	//
	private String Name;
	private String ContactName;
	private String Region;
	private String Address;

	private String Phone;
	//
	private int Type1 = -1;
	private String RentCash;
	//
	private String Rewark;
	//
	private String CarInfo;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		intent = new Intent();
		ed_RentCash.setText("0");
	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_up, R.id.btn_region, R.id.btn_type,
			R.id.btn_nei, R.id.btn_wai })
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
			intent.setClass(SetCustomerInfoActivity2.this,
					ProvinceListActivity.class);
			intent.putExtra("Type",1);
			startActivity(intent);
			break;
		//
		case R.id.btn_type:
			intent.setClass(SetCustomerInfoActivity2.this,
					CustomerTypeListActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_nei:
			Type1 = 2;
			t_type1.setText("室内");
			break;
		//
		case R.id.btn_wai:
			Type1 = 1;
			t_type1.setText("室外");
			break;

		default:
			break;
		}
	}

	private void setData() {
		Name = ed_name.getText().toString();
		ContactName = ed_people.getText().toString();
		Address = ed_address.getText().toString();
		Phone = ed_phone.getText().toString();
		RentCash = ed_RentCash.getText().toString();
		Rewark = ed_remark.getText().toString();
		CarInfo = ed_carinfo.getText().toString();

		Name = Utils.setSrule(Name);
		ContactName = Utils.setSrule(ContactName);
		Address = Utils.setSrule(Address);
		Phone = Utils.setSrule(Phone);
		RentCash = Utils.setSrule(RentCash);
		CarInfo = Utils.setSrule(CarInfo);

		if (Phone == null || Phone.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "联系电话不能为空~");
			return;
		}
		if (Phone.length() != 11) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this,
					"联系电话必须是11位手机号码~");
			return;
		}
		if (Name == null || Name.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "店铺名称不能为空~");
			return;
		}
		if (ContactName == null || ContactName.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "联系人不能为空~");
			return;
		}
		if (Region == null || Region.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "安装地区不能为空~");
			return;
		}
		if (Address == null || Address.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "详细地址不能为空~");
			return;
		}
		if (Type1 == -1) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "还未选择场地类型~");
			return;
		}
		if (Consts.CustomerTypeId == -1) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "还未选择店铺类型~");
			return;
		}

		if (CarInfo == null || CarInfo.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this,
					"设备台数及类型不能为空~");
			return;
		}

		if (RentCash == null || RentCash.equals("")) {
			RentCash = "0";
		}

		if (customer == null) {
			customer = new Customer();
		}
		customer.Phone = Phone;
		customer.North_atitude = 0;
		customer.East_Longitude = 0;
		customer.Name = Name;
		customer.ContactName = ContactName;
		customer.ProvinceId = Consts.Provinceinfo.id;
		customer.ProvinceName = Consts.Provinceinfo.Name;
		customer.CityId = Consts.Cityinfo.id;
		customer.CityName = Consts.Cityinfo.Name;
		customer.RegionId = Consts.Regioninfo.RegionId;
		customer.RegionName = Consts.Regioninfo.Name;
		customer.Address = String.format("%s %s", Address, Name);
		customer.Address = customer.Address.replaceAll("Unknown", "");
		customer.OpenBankName = Name;
		customer.BankCode = "未设置";
		customer.BankHolder = "未设置";
		customer.Remark = Rewark;

		int rentcash = Integer.valueOf(RentCash);
		if (rentcash == 0) {
			customer.RentCash = 0;
			customer.RentType = 0;
		} else {
			customer.RentCash = rentcash;
			customer.RentType = 1;
		}
		customer.Type = Consts.customertypelist.get(Consts.CustomerTypeId).id;
		customer.Type1 = Type1;
		customer.CarInfo = CarInfo;

		UpdateCustomerInfo();
	}

	//
	private void UpdateCustomerInfo() {
		mProgressDialog.show();
		mProgressDialog.setTitle("提交中...");
		String str = URLEncoder.encode(JSON.toJSONString(customer));
		Message m = new Message();
		m.what = 9009;
		CFHttpClient.s().get(
				"?MsgType=9009&mobileType=android&customer=" + str + "&EId="
						+ Consts.user.id, this, m, true);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "信息上传成功~");
			finish();
		} else if (m.arg1 == -2) {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this,
					"手机号码已注册，无法重复注册~");
		} else {
			ToastUtil.showMessages(SetCustomerInfoActivity2.this, "信息上传失败~");
		}
	}

	protected void onResume() {
		super.onResume();
		if (Consts.region_tf) {
			Consts.region_tf = false;
			Region = String.format("%s %s %s", Consts.Provinceinfo.Name,
					Consts.Cityinfo.Name, Consts.Regioninfo.Name);
			if (Region != null && !Region.equals("")) {
				t_region.setText(Region);
			}
		}
		if (Consts.ctype_tf) {
			Consts.ctype_tf = false;
			if (Consts.CustomerTypeId != -1 && Consts.customertypelist != null
					&& Consts.customertypelist.size() != 0) {
				t_typename.setText(Consts.customertypelist
						.get(Consts.CustomerTypeId).TypeName);
			}
		}
	}

}
