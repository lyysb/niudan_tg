package com.niudantg.admin;

import java.io.UnsupportedEncodingException;

import niudantg.BaseActivity;
import org.json.JSONException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.util.Utils;
import com.niudantg.admin.R;
import com.niudantg.http.CFHttpClient_LYY2;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_get_paymentboard)
public class SearchPaymentBoardActivity extends BaseActivity {

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
	@ViewInject(R.id.t_status)
	private TextView t_status;
	//
	@ViewInject(R.id.lay_carinfo)
	private LinearLayout lay_carinfo;
	//
	private String DeviceID;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_select })
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

		default:
			break;
		}
	}

	// 设备情况
	private void getCarInfo() {
		t_status.setText("");
		DeviceID = ed_deviceid.getText().toString();
		DeviceID = Utils.setSrule(DeviceID);
		if (DeviceID == null || DeviceID.equals("")) {
			Toast.makeText(SearchPaymentBoardActivity.this, "支付板编号不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (DeviceID.length() != 5) {
			Toast.makeText(SearchPaymentBoardActivity.this, "支付板编号必须是5位编号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// String[] Codes = { "10001", "10009" };
		try {
			String result = CFHttpClient_LYY2.setData(DeviceID);
			if (result != null && !result.equals("")) {
				ed_deviceid.setText("");
				lay_carinfo.setVisibility(View.VISIBLE);
				String str = java.net.URLDecoder.decode(result, "UTF-8");
				// String[] strs = str.split("ResponseText=");
				// if (strs != null && strs.length != 0) {
				t_status.setText(DeviceID + " : " + str);
				// }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if (DeviceID != null && !DeviceID.equals("")) {
		// try {
		// String result = CFHttpClient_LYY2.setData(DeviceID);
		// if (result != null && !result.equals("")) {
		// ed_deviceid.setText("");
		// lay_carinfo.setVisibility(View.VISIBLE);
		// String str = java.net.URLDecoder.decode(result, "UTF-8");
		// String[] Codes = str.split("ResponseText=");
		// if (Codes != null && Codes.length != 0) {
		// t_status.setText(DeviceID + ":" + Codes[1]);
		// } else {
		// t_status.setText("获取失败");
		// }
		//
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }

	}
}
