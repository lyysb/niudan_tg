package com.niudantg.admin;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import ktx.pojo.domain.FoundPayment;
import ktx.pojo.domain.OrderInfo3;

import niudantg.BaseActivity;
import order.listview.XListView;
import order.listview.XListView.IXListViewListener;
import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.adapter.Order_Adapter;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

@ContentView(R.layout.layout_order)
public class OrderListActivity extends BaseActivity implements CFHttpMsg,
		IXListViewListener {

	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	//
	@ViewInject(R.id.list_order)
	private XListView list_order;
	// 列表适配器
	private Order_Adapter adapter;
	// 收入列表
	private ArrayList<OrderInfo3> datalist = new ArrayList<OrderInfo3>();
	//
	Handler handler = new Handler();
	//
	private FoundPayment found = null;
	// 记录信息条数
	private int order_num = 0;
	// 记录拖拉状态
	private int tl_status = XListView.REFRESH;
	// 检索控制
	private boolean tf = true;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		// 订单listview
		list_order.setPullLoadEnable(true);
		list_order.setPullRefreshEnable(true);
		list_order.setXListViewListener(this);
		list_order.mHeaderView.setHeader(60);

		list_order.mFooterView.setTextNull("还没有记录~");
		list_order.mFooterView.setTextEnd("已是最后的记录了~");
		adapter = new Order_Adapter(this, datalist, handler);
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
		if (found == null) {
			found = new FoundPayment();
		}
		found.TimeType = 1;
		found.Index = order_num;
		String str = URLEncoder.encode(JSON.toJSONString(found));
		mProgressDialog.setTitle("加载中");
		mProgressDialog.show();
		Message m = new Message();
		m.what = 9014;
		CFHttpClient.s().get("?MsgType=9014&mobileType=android&found=" + str,
				this, m, true);

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

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		tf = true;
		if (m.arg1 == 1) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) m.obj;
			@SuppressWarnings("unchecked")
			ArrayList<OrderInfo3> result = (ArrayList<OrderInfo3>) map
					.get("orderlist");
			if (adapter == null) {
				adapter = new Order_Adapter(this, datalist, handler);
				list_order.setAdapter(adapter);
			}
			if (tl_status == XListView.REFRESH) {
				datalist.clear();
				datalist.addAll(result);
			} else {
				datalist.addAll(result);
			}
			adapter.setNewListInfo(datalist);
			adapter.notifyDataSetChanged();
			order_num = datalist.size();
			list_order.stopRefresh();
			list_order.stopLoadMore();
			list_order.setResultSize(result.size(), datalist.size());
		} else {
			Toast.makeText(OrderListActivity.this, "检索失败~", Toast.LENGTH_SHORT)
					.show();
			list_order.stopRefresh();
			list_order.stopLoadMore();
		}
	}

}
