package com.niudantg.admin;

import java.util.ArrayList;
import java.util.Map;
import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.CustomerType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.adapter.CustomerType_Adapter;
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
public class CustomerTypeListActivity extends BaseActivity implements CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.list_region)
	private ListView list_region;
	//
	@ViewInject(R.id.t_title)
	private TextView t_title;
	//
	private int position;

	// 列表适配器
	private CustomerType_Adapter adapter;
	//
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 点击菜品类型
			case 3:
				position = msg.arg1;
				Consts.CustomerTypeId = position;
				Consts.ctype_tf = true;
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
		t_title.setText("商户类型");
		adapter = new CustomerType_Adapter(this, Consts.customertypelist,
				handler);
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
		m.what = 9007;
		CFHttpClient.s().get(
				"?MsgType=9007&mobileType=android&EId=" + Consts.user.id, this,
				m, true);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) m.obj;
			Consts.customertypelist = (ArrayList<CustomerType>) map
					.get("customertypelist");
			if (adapter == null) {
				adapter = new CustomerType_Adapter(this,
						Consts.customertypelist, handler);
				list_region.setAdapter(adapter);
			} else {
				adapter.setNewListInfo(Consts.customertypelist);
				adapter.notifyDataSetChanged();
			}

		} else {
			Toast.makeText(CustomerTypeListActivity.this, "检索失败~",
					Toast.LENGTH_SHORT).show();
		}
	}

}
