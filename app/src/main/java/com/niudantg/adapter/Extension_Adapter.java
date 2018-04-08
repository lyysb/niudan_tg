package com.niudantg.adapter;

import java.util.ArrayList;

import com.niudantg.admin.R;

import ktx.pojo.domain.ExtensionStaff;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Extension_Adapter extends BaseAdapter implements OnClickListener {
	//
	private ArrayList<ExtensionStaff> datalist;
	private Context mContext;
	private Handler handler;

	public Extension_Adapter(Context mContext,
			ArrayList<ExtensionStaff> mqlist, Handler handler) {
		this.mContext = mContext;
		this.datalist = mqlist;
		this.handler = handler;
	}

	public void setNewListInfo(ArrayList<ExtensionStaff> mqlist) {
		this.datalist = mqlist;
	}

	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public ExtensionStaff getItem(int position) {
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
		String Name = datalist.get(position).Name;
		int ID = datalist.get(position).id;
		String Phone = datalist.get(position).Phone;
		int Status = datalist.get(position).Status;
		String CreateTime = datalist.get(position).CreateTime;
		String CityName = datalist.get(position).CityName;

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.lay_extension_item,
					null);
			holder = new Holder();

			holder.lay_1 = (LinearLayout) convertView.findViewById(R.id.lay_1);
			holder.text_1 = (TextView) convertView.findViewById(R.id.text_1);
			holder.text_2 = (TextView) convertView.findViewById(R.id.text_2);
			holder.text_3 = (TextView) convertView.findViewById(R.id.text_3);
			holder.text_4 = (TextView) convertView.findViewById(R.id.text_4);
			holder.text_5 = (TextView) convertView.findViewById(R.id.text_5);
			holder.text_6 = (TextView) convertView.findViewById(R.id.text_6);
			//
			holder.lay_1.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		//
		holder.lay_1.setTag(R.id.lay_1, position);
		//
		holder.text_1.setText(String.format("%d", ID));
		if (Name != null || !"".equals(Name)) {
			holder.text_2.setText(Name);
		}
		if (Phone != null || !"".equals(Phone)) {
			holder.text_3.setText(Phone);
		}
		if (CityName != null || !"".equals(CityName)) {
			holder.text_4.setText(CityName);
		}
		if (Status == 2) {
			holder.text_5.setText("离职");
		} else if (Status == 0) {
			holder.text_5.setText("在职");
		}
		if (CreateTime != null || !"".equals(CreateTime)) {
			holder.text_6.setText(CreateTime);
		}
		return convertView;
	}

	class Holder {
		private LinearLayout lay_1;
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

		default:
			break;
		}

	}

}
