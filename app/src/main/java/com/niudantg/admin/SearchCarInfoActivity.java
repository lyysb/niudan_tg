package com.niudantg.admin;

import java.util.ArrayList;
import java.util.Map;

import ktx.pojo.domain.EquipmentInfo;
import niudantg.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpClient_LYY;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ResultInfo;
import com.niudantg.util.Utils;
import com.niudantg.admin.R;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_carinfo)
public class SearchCarInfoActivity extends BaseActivity implements CFHttpMsg {

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
	@ViewInject(R.id.t_code)
	private TextView t_code;
	//
	@ViewInject(R.id.t_deviceid)
	private TextView t_deviceid;
	//
	@ViewInject(R.id.t_machineid)
	private TextView t_machineid;
	//
	@ViewInject(R.id.t_price)
	private TextView t_price;
	//
	@ViewInject(R.id.t_customerid)
	private TextView t_customerid;
	//
	@ViewInject(R.id.t_address)
	private TextView t_address;
	//
	@ViewInject(R.id.t_status)
	private TextView t_status;
	//
	@ViewInject(R.id.btn_jb)
	private RelativeLayout btn_jb;
	//
	@ViewInject(R.id.lay_carinfo)
	private LinearLayout lay_carinfo;
	//
	private String DeviceID;
	//
	private EquipmentInfo carinfo = new EquipmentInfo();
	//
	private ArrayList<EquipmentInfo> datalist = new ArrayList<EquipmentInfo>();

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
	}

	private void init() {
		if (carinfo != null && carinfo.CustomerId != 0) {
			lay_carinfo.setVisibility(View.VISIBLE);
			if (carinfo.Code != null && !carinfo.Code.equals("")) {
				t_code.setText(carinfo.Code);
			}
			if (carinfo.DeviceID != null && !carinfo.DeviceID.equals("")) {
				t_deviceid.setText(carinfo.DeviceID);
			} else {
				t_deviceid.setText("未设置");
			}
			if (carinfo.MachineID != 0) {
				t_machineid.setText(carinfo.MachineID + "");
			} else {
				t_machineid.setText("未设置");
			}
			t_customerid.setText(carinfo.CustomerId + "");

			if (carinfo.Address != null && !carinfo.Address.equals("")) {
				t_address.setText(String.format("%s %s %s", carinfo.CityName,
						carinfo.RegionName, carinfo.Address));
			}
			t_price.setText(String.format("%.2f 元", carinfo.Price));
		} else {
			lay_carinfo.setVisibility(View.VISIBLE);
			if (carinfo.Code != null && !carinfo.Code.equals("")) {
				t_code.setText(carinfo.Name);
			} else {
				t_code.setText("未设置");
			}
			if (carinfo.DeviceID != null && !carinfo.DeviceID.equals("")) {
				t_deviceid.setText(carinfo.DeviceID);
			} else {
				t_deviceid.setText("未设置");
			}
			if (carinfo.MachineID != 0) {
				t_machineid.setText(carinfo.MachineID + "");
			} else {
				t_machineid.setText("未设置");
			}
			t_customerid.setText("未绑定商户");
			t_address.setText("未设置");
			t_price.setText(String.format("%.2f 元", carinfo.Price));
		}

	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_select, R.id.btn_jb })
	public void viewonclick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;
		//
		case R.id.btn_select:
			getCarInfo();
			break;
		//
		case R.id.btn_jb:
			// if (carinfo != null) {
			// if (carinfo.id != 0 && carinfo.CustomerId != 0) {
			// setCarInfoStatus(carinfo.id, carinfo.CustomerId);
			// }
			// }

			break;

		default:
			break;
		}
	}

	// 设备情况
	private void getCarInfo() {
		DeviceID = ed_deviceid.getText().toString();
		DeviceID = Utils.setSrule(DeviceID);
		if (DeviceID == null || DeviceID.equals("")) {
			Toast.makeText(SearchCarInfoActivity.this, "设备编码不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}

		mProgressDialog.setTitle("加载中");
		mProgressDialog.show();
		Message m = new Message();
		m.what = 9010;
		CFHttpClient.s().get(
				"?MsgType=9010&mobileType=android&DeviceID=" + DeviceID, this,
				m, true);

	}

	// // 解绑
	// private void setCarInfoStatus(int CarId, int CustomerId) {
	//
	// mProgressDialog.setTitle("提交中");
	// mProgressDialog.show();
	// Message m = new Message();
	// m.what = 9016;
	// CFHttpClient.s().get(
	// "?MsgType=9016&mobileType=android&CarId=" + CarId
	// + "&CustomerId=" + CustomerId, this, m, true);
	//
	// }

	public void httpMsg(Message m) {

		switch (m.what) {
		//
		case 9010:
			if (m.arg1 == 1 || m.arg1 == -3) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) m.obj;
				carinfo = (EquipmentInfo) map.get("eqinfo");
				datalist.add(carinfo);
				if (datalist != null && datalist.size() != 0) {
					String result;
					try {
						result = CFHttpClient_LYY.setData(datalist);
						if (result != null && !result.equals("")) {
							JSONObject jo = new JSONObject(result);
							int result1 = jo.getInt("result");

							if (result1 == 0) {
								ArrayList<ResultInfo> resultlist = (ArrayList<ResultInfo>) JSON
										.parseArray(jo.getString("data"),
												ResultInfo.class);
								for (EquipmentInfo car : datalist) {
									for (int i = 0; i < resultlist.size(); i++) {
										int result2 = resultlist.get(i).result;
										String device_info = resultlist.get(i).device_info;
										String description = resultlist.get(i).description;
										if (description != null
												&& !description.equals("")) {
											t_status.setText(description);
										}
									}
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				mProgressDialog.cancel();
				init();
			} else if (m.arg1 == -2) {
				mProgressDialog.cancel();
				Toast.makeText(SearchCarInfoActivity.this, "未检索到该设备~",
						Toast.LENGTH_SHORT).show();
				lay_carinfo.setVisibility(View.GONE);
			} else {
				mProgressDialog.cancel();
				Toast.makeText(SearchCarInfoActivity.this, "检索失败~",
						Toast.LENGTH_SHORT).show();
				lay_carinfo.setVisibility(View.GONE);
			}
			break;
		// //
		// case 9016:
		// if (m.arg1 == 1) {
		// ed_deviceid.setText("");
		// lay_carinfo.setVisibility(View.GONE);
		// Toast.makeText(SearchCarInfoActivity.this, "解绑成功~",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(SearchCarInfoActivity.this, "提交失败~",
		// Toast.LENGTH_SHORT).show();
		// }
		// break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

}
