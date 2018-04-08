package com.niudantg.admin;

import java.util.ArrayList;
import java.util.Map;

import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.Customer;
import order.listview.XListView;
import order.listview.XListView.IXListViewListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.adapter.Customer_Adapter;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.admin.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_customerlist)
public class CustomerListActivity extends BaseActivity implements CFHttpMsg,
		IXListViewListener {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.list_customer)
	private XListView list_order;
	//
	@ViewInject(R.id.t_customernum)
	private TextView t_customernum;

	// 列表适配器
	private Customer_Adapter adapter;
	//
	private int position;
	// 记录信息条数
	private int order_num = 0;
	// 记录拖拉状态
	private int tl_status = XListView.REFRESH;
	// 检索控制
	private boolean tf = true;
	// 订单列表
	private ArrayList<Customer> datallist = new ArrayList<Customer>();
	//
	private Intent intent;
	//
	private int customernum;
	//
	private int EId;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 点击选中
			case 3:
				position = msg.arg1;
				if (datallist.get(position).id != 0
						&& datallist.get(position).Level != 3
						&& datallist.get(position).Level != 4) {
					Consts.customer = datallist.get(position);
					intent.setClass(CustomerListActivity.this,
							CarListActivity.class);
					intent.putExtra("CustomerId", datallist.get(position).id);
					startActivity(intent);
				}

				break;
			//
			case 6:
				position = msg.arg1;
				if (datallist.get(position).Phone != null
						&& !datallist.get(position).Phone.equals("")) {
					intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:"
							+ datallist.get(position).Phone));
					startActivity(intent);
				}
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
		EId = this.getIntent().getIntExtra("EId", 0);
		intent = new Intent();
		// 订单listview
		list_order.setPullLoadEnable(true);
		list_order.setPullRefreshEnable(true);
		list_order.setXListViewListener(this);
		list_order.mHeaderView.setHeader(60);

		list_order.mFooterView.setTextNull("还没有记录~");
		list_order.mFooterView.setTextEnd("已是最后的记录了~");
		adapter = new Customer_Adapter(this, datallist, handler);
		list_order.setAdapter(adapter);

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

	// 获取订单list
	private void getOrderList() {

		mProgressDialog.setTitle("加载中");
		mProgressDialog.show();
		Message m = new Message();
		m.what = 9008;
		CFHttpClient.s().get(
				"?MsgType=9008&mobileType=android&EId=" + EId + "&Index="
						+ order_num, this, m, true);

	}

	private void loadData(final int what) {
		tl_status = what;
		getOrderList();

	}

	public void onRefresh() {
		if (tf) {
			order_num = 0;
			loadData(XListView.REFRESH);
			tf = false;
		}
	}

	public void onLoadMore() {
		if (tf) {
			loadData(XListView.LOAD);
			tf = false;
		}

	}

	@Override
	public void httpMsg(Message m) {
		switch (m.what) {
			//
			case 9008:
				mProgressDialog.cancel();
				tf = true;
				if (m.arg1 == 1) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) m.obj;
					@SuppressWarnings("unchecked")
					ArrayList<Customer> result = (ArrayList<Customer>) map
							.get("customerlist");
					customernum = (Integer) map.get("customernum");
					t_customernum.setText(String.format("%d户商家", customernum));
					if (adapter == null) {
						adapter = new Customer_Adapter(this, datallist, handler);
						list_order.setAdapter(adapter);
					}
					if (tl_status == XListView.REFRESH) {
						datallist.clear();
						datallist.addAll(result);
					} else {
						datallist.addAll(result);
					}
					adapter.setNewListInfo(datallist);
					adapter.notifyDataSetChanged();
					order_num = datallist.size();
					list_order.stopRefresh();
					list_order.stopLoadMore();
					list_order.setResultSize(result.size(), datallist.size());
				} else {
					Toast.makeText(CustomerListActivity.this, "检索失败~",
							Toast.LENGTH_SHORT).show();
					list_order.stopRefresh();
					list_order.stopLoadMore();
				}
				break;

			default:
				break;
		}

	}

}
