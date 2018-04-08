package com.niudantg.admin;

import java.util.Map;
import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.Customer;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_car_bd)
public class BDCarActivity extends BaseActivity implements CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.btn_select)
	private RelativeLayout btn_select;
	//
	@ViewInject(R.id.ed_deviceid)
	private EditText ed_deviceid;
	//
	@ViewInject(R.id.ed_phone)
	private EditText ed_phone;
	//
	@ViewInject(R.id.btn_select1)
	private RelativeLayout btn_select1;

	//
	@ViewInject(R.id.t_id)
	private TextView t_id;
	//
	@ViewInject(R.id.t_name)
	private TextView t_name;
	//
	@ViewInject(R.id.t_people)
	private TextView t_people;
	//
	@ViewInject(R.id.t_phone)
	private TextView t_phone;
	//
	@ViewInject(R.id.t_address)
	private TextView t_address;
	//
	@ViewInject(R.id.t_status)
	private TextView t_status;
	//
	@ViewInject(R.id.t_wx_status)
	private TextView t_wx_status;
	//
	@ViewInject(R.id.t_memo)
	private TextView t_memo;
	//
	@ViewInject(R.id.btn_carlist)
	private RelativeLayout btn_carlist;
	//
	@ViewInject(R.id.btn_updatephone)
	private RelativeLayout btn_updatephone;
	//
	@ViewInject(R.id.lay_carinfo)
	private LinearLayout lay_carinfo;

	//
	private String ID;
	//
	private Intent intent;
	//
	private String Phone;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		intent = new Intent();
	}

	private void init() {
		if (Consts.customer != null) {
			lay_carinfo.setVisibility(View.VISIBLE);
			t_id.setText("" + Consts.customer.id);
			if (Consts.customer.OpenBankName != null
					&& !Consts.customer.OpenBankName.equals("")) {
				t_name.setText(Consts.customer.OpenBankName);
			}
			if (Consts.customer.ContactName != null
					&& !Consts.customer.ContactName.equals("")) {
				t_people.setText(Consts.customer.ContactName);
			}
			if (Consts.customer.Phone != null
					&& !Consts.customer.Phone.equals("")) {
				t_phone.setText(Consts.customer.Phone);
			}

			if (Consts.customer.Address != null
					&& !Consts.customer.Address.equals("")) {
				t_address.setText(String.format("%s %s %s %s",
						Consts.customer.ProvinceName, Consts.customer.CityName,
						Consts.customer.RegionName, Consts.customer.Address));
			}
			switch (Consts.customer.Level) {
			//
			case 0:
				t_status.setText("运营中");
				btn_carlist.setVisibility(View.VISIBLE);
				break;
			//
			case 1:
				t_status.setText("已下线");
				btn_carlist.setVisibility(View.VISIBLE);
				break;
			//
			case 2:
				t_status.setText("待铺设");
				btn_carlist.setVisibility(View.VISIBLE);
				break;
			//
			case 3:
				t_status.setText("待审核");
				btn_carlist.setVisibility(View.GONE);
				break;
			//
			case 4:
				t_status.setText("审核失败");
				btn_carlist.setVisibility(View.GONE);
				break;

			default:
				break;
			}
			if (Consts.customer.WxId != null
					&& !Consts.customer.WxId.equals("")) {
				t_wx_status.setText("已绑定");
			} else {
				t_wx_status.setText("未绑定");
			}
			if (Consts.customer.Remark != null
					&& !Consts.customer.Remark.equals("")) {
				if (Consts.customer.CarInfo != null
						&& !Consts.customer.CarInfo.equals("")) {
					t_memo.setText(String.format("%s;%s",
							Consts.customer.Remark, Consts.customer.CarInfo));
				} else {
					t_memo.setText(Consts.customer.Remark);
				}

			} else {
				if (Consts.customer.CarInfo != null
						&& !Consts.customer.CarInfo.equals("")) {
					t_memo.setText(Consts.customer.CarInfo);
				} else {
					t_memo.setText("无备注");
				}

			}

		} else {
			lay_carinfo.setVisibility(View.GONE);
		}

	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_select, R.id.btn_carlist,
			R.id.btn_updatephone, R.id.btn_select1 })
	public void viewonclick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;
		//
		case R.id.btn_select1:
			getCustomerToID();
			break;
		//
		case R.id.btn_select:
			getCustomerToPhone();
			break;
		//
		case R.id.btn_carlist:
			if (Consts.customer.id != 0) {
				intent.setClass(BDCarActivity.this, CarListActivity.class);
				intent.putExtra("CustomerId", Consts.customer.id);
				startActivity(intent);
			}
			break;
		//
		case R.id.btn_updatephone:
			intent.setClass(BDCarActivity.this, UpdatePhoneActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	// 设备情况
	private void getCustomerToID() {
		ID = ed_deviceid.getText().toString();
		ID = Utils.setSrule(ID);
		if (ID == null || ID.equals("")) {
			Toast.makeText(BDCarActivity.this, "商户ID不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		mProgressDialog.setTitle("加载中");
		mProgressDialog.show();
		Message m = new Message();
		m.what = 9004;
		CFHttpClient.s().get(
				"?MsgType=9004&mobileType=android&CustomerId=" + ID + "&EId="
						+ Consts.user.id + "&Type=1", this, m, true);

	}

	// 获取商户信息
	private void getCustomerToPhone() {
		Phone = ed_phone.getText().toString();
		Phone = Utils.setSrule(Phone);
		if (Phone == null || Phone.equals("")) {
			Toast.makeText(BDCarActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (Phone.length() != 11) {
			Toast.makeText(BDCarActivity.this, "手机号码必须是11位", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// Type=0无权限，1有权限
		mProgressDialog.show();
		Message message = new Message();
		message.what = 9003;
		CFHttpClient.s().get(
				"?MsgType=9003&mobileType=android&Phone=" + Phone + "&EId="
						+ Consts.user.id + "&Type=1", this, message, false);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		switch (m.what) {
		//
		case 9003:
			mProgressDialog.cancel();
			if (m.arg1 == 1) {
				Map<String, Object> map = (Map<String, Object>) m.obj;
				Consts.customer = (Customer) map.get("customer");
				ed_phone.setText("");
				init();
			} else if (m.arg1 == -2) {
				ToastUtil.showMessages(BDCarActivity.this, "当前账号不存在");
			} else {
				ToastUtil.showMessages(BDCarActivity.this, "检索失败，请重试~");
			}
			break;
		//
		case 9004:
			if (m.arg1 == 1) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) m.obj;
				Consts.customer = (Customer) map.get("customer");
				ed_deviceid.setText("");
				init();
			} else if (m.arg1 == -2) {
				lay_carinfo.setVisibility(View.GONE);
				Toast.makeText(BDCarActivity.this, "未检索到该商户~",
						Toast.LENGTH_SHORT).show();
			} else if (m.arg1 == -3) {
				lay_carinfo.setVisibility(View.GONE);
				Toast.makeText(BDCarActivity.this, "您无权限查看该商户信息~",
						Toast.LENGTH_SHORT).show();
			} else {
				lay_carinfo.setVisibility(View.GONE);
				Toast.makeText(BDCarActivity.this, "检索失败~", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

}
