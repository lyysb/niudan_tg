package com.niudantg.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.ExtensionStaff;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.adapter.Extension_Adapter;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.Comparator_Extension;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_extension)
public class ExtensionActivity extends BaseActivity implements CFHttpMsg {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.list_extension)
	private ListView list_extension;
	//
	@ViewInject(R.id.t_freedata)
	private TextView t_freedata;
	//
	@ViewInject(R.id.btn_new)
	private Button btn_new;

	// 列表适配器
	private Extension_Adapter adapter;
	//
	private int position;
	//
	private Intent intent;
	// 收入列表
	private ArrayList<ExtensionStaff> datalist = new ArrayList<ExtensionStaff>();
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 点击选中
			case 3:
				position = msg.arg1;
				setQuerenDialog(position);
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
		getDataList();
	}

	private void init() {
		intent = new Intent();
		adapter = new Extension_Adapter(this, datalist, handler);
		list_extension.setAdapter(adapter);
	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_new })
	public void viewonclick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;
		//
		case R.id.btn_new:
			intent.setClass(ExtensionActivity.this,
					NewExtensionStaffActivity.class);
			intent.putExtra("EId", 0);
			intent.putExtra("Name", "");
			intent.putExtra("Phone", "");
			intent.putExtra("Password", "123456");
			intent.putExtra("Level", 0);
			intent.putExtra("BD_Status", 0);
			intent.putExtra("CityName", "未设置");
			intent.putExtra("CityId", 0);
			intent.putExtra("Type", 0);
			intent.putExtra("Status", 0);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	// 获取推广人员列表
	private void getDataList() {
		mProgressDialog.setTitle("加载中");
		mProgressDialog.show();
		Message m = new Message();
		m.what = 9024;
		CFHttpClient.s().get(
				"?MsgType=9024&mobileType=android&EId=" + Consts.user.id, this,
				m, true);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) m.obj;
			datalist = (ArrayList<ExtensionStaff>) map.get("EsList");
			if (datalist != null) {
				Comparator_Extension comparator = new Comparator_Extension();
				Collections.sort(datalist, comparator);
			}
			if (adapter == null) {
				adapter = new Extension_Adapter(this, datalist, handler);
				list_extension.setAdapter(adapter);
			} else {
				adapter.setNewListInfo(datalist);
				adapter.notifyDataSetChanged();
			}
			if (datalist != null && datalist.size() != 0) {
				t_freedata.setVisibility(View.INVISIBLE);
				list_extension.setVisibility(View.VISIBLE);
			} else {
				t_freedata.setVisibility(View.VISIBLE);
				list_extension.setVisibility(View.INVISIBLE);
			}

		} else if (m.arg1 == 2) {
			t_freedata.setVisibility(View.VISIBLE);
			list_extension.setVisibility(View.INVISIBLE);
		} else {
			t_freedata.setVisibility(View.VISIBLE);
			list_extension.setVisibility(View.INVISIBLE);
			Toast.makeText(ExtensionActivity.this, "检索失败~", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Consts.newef_tf) {
			Consts.newef_tf = false;
			getDataList();
		}
	}

	// 确定界面
	protected void setQuerenDialog(final int Position) {

		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		dlg.setCanceledOnTouchOutside(false);
		Window window = dlg.getWindow();

		window.setContentView(R.layout.dialog_extension);

		// 名下商户列表
		TextView btn_1 = (TextView) window.findViewById(R.id.btn_1);
		// 修改商户信息
		TextView btn_2 = (TextView) window.findViewById(R.id.btn_2);
		// 取消
		TextView btn_3 = (TextView) window.findViewById(R.id.btn_3);

		btn_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (datalist.get(position).id != 0) {
					intent.setClass(ExtensionActivity.this,
							CustomerListActivity.class);
					intent.putExtra("EId", datalist.get(Position).id);
					startActivity(intent);
				}
				dlg.cancel();
			}
		});

		btn_2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (datalist.get(position).id != 0) {
					intent.setClass(ExtensionActivity.this,
							NewExtensionStaffActivity.class);
					intent.putExtra("EId", datalist.get(Position).id);
					intent.putExtra("Name", datalist.get(Position).Name);
					intent.putExtra("Phone", datalist.get(Position).Phone);
					intent.putExtra("Password", datalist.get(Position).Password);
					intent.putExtra("Level", datalist.get(Position).Level);
					intent.putExtra("BD_Status",
							datalist.get(Position).BD_Status);
					intent.putExtra("CityName", datalist.get(Position).CityName);
					intent.putExtra("CityId", datalist.get(Position).CityId);
					intent.putExtra("Type", datalist.get(Position).Type);
					intent.putExtra("Status", datalist.get(Position).Status);
					startActivity(intent);
				}
				dlg.cancel();
			}
		});

		btn_3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});

	}

}
