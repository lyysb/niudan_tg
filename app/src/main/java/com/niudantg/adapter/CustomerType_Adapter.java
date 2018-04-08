package com.niudantg.adapter;

import java.util.ArrayList;
import com.niudantg.admin.R;
import ktx.pojo.domain.CustomerType;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomerType_Adapter extends BaseAdapter implements
		OnClickListener {
	//
	private ArrayList<CustomerType> datalist;
	private Context mContext;
	private Handler handler;

	public CustomerType_Adapter(Context mContext,
			ArrayList<CustomerType> mqlist, Handler handler) {
		this.mContext = mContext;
		this.datalist = mqlist;
		this.handler = handler;
	}

	public void setNewListInfo(ArrayList<CustomerType> mqlist) {
		this.datalist = mqlist;
	}

	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public CustomerType getItem(int position) {
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;

		// 名称
		String Name = datalist.get(position).TypeName;

		if (convertView == null) {
			convertView = View
					.inflate(mContext, R.layout.lay_region_item, null);
			holder = new Holder();

			holder.lay_1 = (LinearLayout) convertView.findViewById(R.id.lay_1);
			holder.t_name = (TextView) convertView.findViewById(R.id.t_name);

			holder.lay_1.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		//
		holder.lay_1.setTag(R.id.lay_1, position);
		//
		if (Name != null || !"".equals(Name)) {
			holder.t_name.setText(Name);
		}

		return convertView;
	}

	class Holder {
		private LinearLayout lay_1;
		private TextView t_name;
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

		default:
			break;
		}

	}

}
