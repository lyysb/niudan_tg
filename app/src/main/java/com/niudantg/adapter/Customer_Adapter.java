package com.niudantg.adapter;

import java.util.ArrayList;
import com.niudantg.admin.R;
import ktx.pojo.domain.Customer;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Customer_Adapter extends BaseAdapter implements OnClickListener {
	//
	private ArrayList<Customer> datalist;
	private Context mContext;
	private Handler handler;

	public Customer_Adapter(Context mContext, ArrayList<Customer> mqlist,
			Handler handler) {
		this.mContext = mContext;
		this.datalist = mqlist;
		this.handler = handler;
	}

	public void setNewListInfo(ArrayList<Customer> mqlist) {
		this.datalist = mqlist;
	}

	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public Customer getItem(int position) {
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;

		int id = datalist.get(position).id;
		// 名称
		String Phone = datalist.get(position).Phone;
		String ContactName = datalist.get(position).ContactName;
		//
		String OpenBankName = datalist.get(position).OpenBankName;
		//
		String CreateTime = datalist.get(position).CreateTime;
		//
		int Level = datalist.get(position).Level;
		//
		String ProvinceName = datalist.get(position).ProvinceName;
		String CityName = datalist.get(position).CityName;
		String RegionName = datalist.get(position).RegionName;
		String Address = datalist.get(position).Address;

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.lay_customer_item,
					null);
			holder = new Holder();

			holder.lay_1 = (LinearLayout) convertView.findViewById(R.id.lay_1);
			holder.text_0 = (TextView) convertView.findViewById(R.id.text_0);
			holder.text_1 = (TextView) convertView.findViewById(R.id.text_1);
			holder.text_2 = (TextView) convertView.findViewById(R.id.text_2);
			holder.text_3 = (TextView) convertView.findViewById(R.id.text_3);
			holder.text_4 = (TextView) convertView.findViewById(R.id.text_4);
			holder.text_5 = (TextView) convertView.findViewById(R.id.text_5);
			holder.text_6 = (TextView) convertView.findViewById(R.id.text_6);
			holder.lay_1.setOnClickListener(this);
			holder.text_2.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.lay_1.setTag(R.id.lay_1, position);
		holder.text_2.setTag(R.id.text_2, position);

		holder.text_0.setText(String.format("ID:%d", id));
		//
		if (OpenBankName != null || !"".equals(OpenBankName)) {
			holder.text_1.setText(OpenBankName);
		} else {
			holder.text_1.setText("未设置");
		}
		if (Phone != null || !"".equals(Phone)) {
			holder.text_2.setText(Html.fromHtml(String.format("<u>%s</u>",
					Phone)));
		}
		if (ContactName != null || !"".equals(ContactName)) {
			holder.text_3.setText(ContactName);
		}
		if (CreateTime != null || !"".equals(CreateTime)) {
			holder.text_4.setText(CreateTime);
		}
		if (Level == 0) {
			holder.text_5.setText("运营");
			holder.text_5.setTextColor(mContext.getResources().getColor(
					R.color.red));
		} else if (Level == 1) {
			holder.text_5.setText("下线");
			holder.text_5.setTextColor(mContext.getResources().getColor(
					R.color.gray));
		} else if (Level == 2) {
			holder.text_5.setText("待铺设");
			holder.text_5.setTextColor(mContext.getResources().getColor(
					R.color.black));
		} else if (Level == 3) {
			holder.text_5.setText("待审核");
			holder.text_5.setTextColor(mContext.getResources().getColor(
					R.color.black));
		} else if (Level == 4) {
			holder.text_5.setText("审核失败");
			holder.text_5.setTextColor(mContext.getResources().getColor(
					R.color.black));
		}
		if (Address != null || !"".equals(Address)) {
			holder.text_6.setText(String.format("%s %s %s %s", ProvinceName,
					CityName, RegionName, Address));
		}

		return convertView;
	}

	class Holder {
		private LinearLayout lay_1;
		private TextView text_0;
		private TextView text_1;
		private TextView text_2;
		private TextView text_3;
		private TextView text_4;
		private TextView text_5;
		private TextView text_6;
	}

	public void onClick(View v) {
		Message msg = Message.obtain();
		int position;
		switch (v.getId()) {
		// 点击效果
		case R.id.lay_1:
			position = (Integer) v.getTag(R.id.lay_1);
			msg.what = 3;
			msg.arg1 = position;
			handler.sendMessage(msg);
			break;
		// 点击效果
		case R.id.text_2:
			position = (Integer) v.getTag(R.id.text_2);
			msg.what = 6;
			msg.arg1 = position;
			handler.sendMessage(msg);
			break;

		default:
			break;
		}

	}

}
