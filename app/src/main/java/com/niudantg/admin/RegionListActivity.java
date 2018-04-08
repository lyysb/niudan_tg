package com.niudantg.admin;

import java.util.ArrayList;
import java.util.Map;

import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.RegionInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.adapter.Region_Adapter;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.admin.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_regionlist)
public class RegionListActivity extends BaseActivity implements CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.list_region)
	private ListView list_region;
	//
	@ViewInject(R.id.btn_all)
	private TextView btn_all;
	//
	private int position;
	//
	private int Id = 0;

	// 列表适配器
	private Region_Adapter adapter;
	// 收入列表
	private ArrayList<RegionInfo> datalist = new ArrayList<RegionInfo>();
	//
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 点击菜品类型
			case 3:
				position = msg.arg1;
				Consts.Regioninfo = datalist.get(position);
				Consts.region_tf = true;
				finish();
				break;
			default:
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		btn_all.setVisibility(View.GONE);
		Id = getIntent().getIntExtra("Id", 0);
		adapter = new Region_Adapter(this, datalist, handler);
		list_region.setAdapter(adapter);
		getCarList();
	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back })
	public void viewonclick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	// 获取设备list
	private void getCarList() {
		mProgressDialog.setTitle("加载中");
		mProgressDialog.show();
		Message m = new Message();
		m.what = 9013;
		CFHttpClient.s().get(
				"?MsgType=9013&mobileType=android&Id=" + Id + "&Type=3", this,
				m, true);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) m.obj;
			datalist = (ArrayList<RegionInfo>) map.get("regionlist");
			if (adapter == null) {
				adapter = new Region_Adapter(this, datalist, handler);
				list_region.setAdapter(adapter);
			} else {
				adapter.setNewListInfo(datalist);
				adapter.notifyDataSetChanged();
			}

		} else {
			Toast.makeText(RegionListActivity.this, "检索失败~", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
