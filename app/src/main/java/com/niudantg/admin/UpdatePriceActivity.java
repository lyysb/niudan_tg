package com.niudantg.admin;

import java.net.URLEncoder;

import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.UpdatePrice;
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

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//修改价格界面
@ContentView(R.layout.layout_updateprice)
public class UpdatePriceActivity extends BaseActivity implements CFHttpMsg {
	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.t_carname)
	private TextView t_carname;
	//
	@ViewInject(R.id.t_oldprice)
	private TextView t_oldprice;
	//
	@ViewInject(R.id.ed_price)
	private EditText ed_price;
	// 登陆按钮
	@ViewInject(R.id.btn_up)
	private RelativeLayout btn_up;
	//
	private UpdatePrice updateprice;
	//
	private String CarName;
	private int CarId;
	private float Price;
	private float newprice = 1;
	//
	private Bundle bundle = new Bundle();

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		Consts.updateprice_tf = false;
		bundle = this.getIntent().getExtras();
		CarName = bundle.getString("CarName");
		CarId = bundle.getInt("CarId");
		Price = bundle.getFloat("Price");
		if (CarName != null && !CarName.equals("")) {
			t_carname.setText(CarName);
		}
		t_oldprice.setText(String.format("%.1f元/次", Price));
		ed_price.setText("1");
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
						ToastUtil.showMessages(UpdatePriceActivity.this,
								"网络连接失败 ,请检打开网络!");
					}
				}
			} else {
				touchTime = currentTime;
				if (CheckNetworkState()) {
					setData();
				} else {
					ToastUtil.showMessages(UpdatePriceActivity.this,
							"网络连接失败 ,请检打开网络!");
				}
			}
			break;

		default:
			break;
		}
	}

	private void setData() {
		String s_newprice = ed_price.getText().toString();
		s_newprice = Utils.setSrule(s_newprice);
		if (s_newprice == null || s_newprice.equals("")) {
			ToastUtil.showMessages(UpdatePriceActivity.this, "新价格不能为空~");
		}
		if (!s_newprice.matches("\\d+\\.\\d+") && !s_newprice.matches("\\d+")) {
			ToastUtil.showMessages(UpdatePriceActivity.this, "价格的格式不正确,请检查~");
			return;
		}
		newprice = Float.valueOf(s_newprice);
		if (newprice <= 0) {
			ToastUtil.showMessages(UpdatePriceActivity.this, "价格不能等于0~");
			return;
		}
		if (Price == newprice) {
			ToastUtil.showMessages(UpdatePriceActivity.this, "新价格与原价相同，不能提交~");
			return;
		}

		if (updateprice == null) {
			updateprice = new UpdatePrice();
		}
		updateprice.CustomerId = Consts.user.id;
		updateprice.CarId = CarId;
		updateprice.Price = newprice;
		updateprice.EId = Consts.user.id;

		mProgressDialog.setTitle("提交中...");
		mProgressDialog.show();

		String str = URLEncoder.encode(JSON.toJSONString(updateprice));
		Message message = new Message();
		message.what = 9012;
		CFHttpClient.s().get(
				"?MsgType=9012&mobileType=android&updateprice=" + str, this,
				message, false);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void httpMsg(Message m) {
		switch (m.what) {
		//
		case 9012:
			mProgressDialog.cancel();
			if (m.arg1 == 1) {
				ToastUtil.showMessages(UpdatePriceActivity.this, "设置成功~");
				Consts.updateprice_tf = true;
				finish();
			} else {
				ToastUtil.showMessages(UpdatePriceActivity.this, "设置失败，请重试~");
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
