package com.niudantg.admin;

import java.util.Map;

import ktx.pojo.domain.StatisticsInfo;

import niudantg.BaseActivity;
import niudantg.Consts;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ToastUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@ContentView(R.layout.layout_statistics)
public class StatisticsActivity extends BaseActivity implements CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.text_1)
	private TextView text_1;
	//
	@ViewInject(R.id.text_2)
	private TextView text_2;
	//
	@ViewInject(R.id.text_3)
	private TextView text_3;
	//
	@ViewInject(R.id.text_4)
	private TextView text_4;
	//
	@ViewInject(R.id.text_5)
	private TextView text_5;
	//
	@ViewInject(R.id.text_6)
	private TextView text_6;
	//
	@ViewInject(R.id.text_7)
	private TextView text_7;
	//
	@ViewInject(R.id.text_8)
	private TextView text_8;
	//
	@ViewInject(R.id.text_9)
	private TextView text_9;
	//
	@ViewInject(R.id.text_10)
	private TextView text_10;
	//
	@ViewInject(R.id.text_11)
	private TextView text_11;
	//
	@ViewInject(R.id.text_12)
	private TextView text_12;
	//
	@ViewInject(R.id.text_13)
	private TextView text_13;
	//
	@ViewInject(R.id.text_14)
	private TextView text_14;
	// //
	// @ViewInject(R.id.btn_city)
	// private TextView btn_city;
	// //
	// private int CityId = 0;
	// private String CityName = "全国";
	//
	private Intent intent;

	//
	private StatisticsInfo statisticsInfo = new StatisticsInfo();

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		getData();
	}

	private void init() {
		intent = new Intent();

		text_1.setText(String.format("今日 %d 单", statisticsInfo.ATOrderNum));
		text_2.setText(String.format("昨日 %d 单", statisticsInfo.AYOrderNum));
		text_3.setText(String.format("今日  %d 单", statisticsInfo.TSOrderNum));
		text_4.setText(String.format("昨日  %d 单", statisticsInfo.YSOrderNum));
		text_5.setText(String.format("今日  %d 单", statisticsInfo.TROrderNum));
		text_6.setText(String.format("昨日  %d 单", statisticsInfo.YROrderNum));
		text_7.setText(String.format("今日  %.1f 元", statisticsInfo.TodayCash));
		text_8.setText(String
				.format("昨日  %.1f 元", statisticsInfo.YesterdayCash));
		text_9.setText(String.format("今日 %d 人", statisticsInfo.TAddUserNum));
		text_10.setText(String.format("昨日  %d 次", statisticsInfo.YAddUserNum));
		text_11.setText(String.format("%d 人", statisticsInfo.UserNum));
		text_12.setText(String.format(" %d 台", statisticsInfo.ENum));
		text_13.setText(String.format("%d 单", statisticsInfo.OrderNum));
		text_14.setText(String.format(" %.1f 元", statisticsInfo.OrderCash));

	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back })
	public void viewonclick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;
		//
		// case R.id.btn_city:
		// intent.setClass(StatisticsActivity.this, ProvinceListActivity.class);
		// startActivity(intent);
		// break;

		default:
			break;
		}
	}

	private void getData() {
		mProgressDialog.show();
		Message message = new Message();
		message.what = 9017;
		CFHttpClient.s().get(
				"?MsgType=9017&mobileType=android&UserId=" + Consts.user.id
						+ "&CityId=0", this, message, false);
	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1) {
			Map<String, Object> map = (Map<String, Object>) m.obj;
			statisticsInfo = (StatisticsInfo) map.get("statisticsinfo");
			init();
		} else {
			ToastUtil.showMessages(StatisticsActivity.this, "获取信息失败~");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (Consts.region_tf) {
		// Consts.region_tf = false;
		// CityName = Consts.Cityinfo.Name;
		// CityId = Consts.Cityinfo.id;
		// if (CityName != null && !CityName.equals("")) {
		// btn_city.setText(CityName);
		// getData();
		// }
		// }
	}
}
