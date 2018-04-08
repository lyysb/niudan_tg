package com.niudantg.adapter;

import java.util.ArrayList;

import niudantg.Application;

import com.niudantg.admin.R;

import ktx.pojo.domain.OrderInfo3;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Order_Adapter extends BaseAdapter {
	//
	private ArrayList<OrderInfo3> datalist;
	private Context mContext;
	private Handler handler;

	public Order_Adapter(Context mContext, ArrayList<OrderInfo3> mqlist,
			Handler handler) {
		this.mContext = mContext;
		this.datalist = mqlist;
		this.handler = handler;
	}

	public void setNewListInfo(ArrayList<OrderInfo3> mqlist) {
		this.datalist = mqlist;
	}

	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public OrderInfo3 getItem(int position) {
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;

		// 用户昵称
		String user_name = datalist.get(position).Name;
		//
		String Image = datalist.get(position).Image;
		//
		float Cash = datalist.get(position).Cash;
		//
		int PayStatus = datalist.get(position).PayStatus;
		//
		int PlayStatus = datalist.get(position).PlayStatus;
		//
		String CreateTime = datalist.get(position).CreateTime;
		//
		String DeviceID = datalist.get(position).DeviceID;
		//
		String CityName = datalist.get(position).CityName;
		String RegionName = datalist.get(position).RegionName;
		//
		int refundstatus = datalist.get(position).RefundStatus;

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.lay_order_item, null);
			holder = new Holder();

			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.text_2 = (TextView) convertView.findViewById(R.id.text_2);
			holder.text_3 = (TextView) convertView.findViewById(R.id.text_3);
			holder.text_4 = (TextView) convertView.findViewById(R.id.text_4);
			holder.text_5 = (TextView) convertView.findViewById(R.id.text_5);
			holder.text_6 = (TextView) convertView.findViewById(R.id.text_6);
			holder.text_7 = (TextView) convertView.findViewById(R.id.text_7);
			holder.text_8 = (TextView) convertView.findViewById(R.id.text_8);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		if (Image == null || "".equals(Image)) {

			holder.img.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.empty_photo));
		} else {
			Application.mBU_Head.display(holder.img, Image);
		}
		//
		if (user_name != null && !"".equals(user_name)) {
			holder.text_2.setText(user_name);
		}
		holder.text_3.setText(String.format(" %.2f", Cash));

		switch (PlayStatus) {
		//
		case 0:
			holder.text_5.setText("未启动");
			break;
		//
		case 1:
			holder.text_5.setText("已启动");
			break;

		default:
			break;
		}
		switch (PayStatus) {
		//
		case 0:
			holder.text_4.setText("未支付");
			break;
		//
		case 1:
			holder.text_4.setText("微信支付");
			break;
		//
		case 2:
			holder.text_4.setText("兑换码支付");
			break;
		//
		case 3:
			holder.text_4.setText("京东支付");
			break;
		//
		case 4:
			holder.text_4.setText("月卡支付");
			break;

		default:
			break;
		}

		if (refundstatus == 10) {
			holder.text_4.setText("已退款");
		}
		if (CreateTime != null && !"".equals(CreateTime)) {
			holder.text_6.setText(CreateTime);
		}
		//
		if (DeviceID != null && !"".equals(DeviceID)) {
			holder.text_7.setText(DeviceID);
		}
		//
		if (CityName != null && !"null".equals(CityName)
				&& !"".equals(CityName)) {
			holder.text_8.setText(String.format("%s%s", CityName, RegionName));
		} else {
			holder.text_8.setText("未绑定商家");
		}
		return convertView;
	}

	class Holder {

		private ImageView img;
		private TextView text_2;
		private TextView text_3;
		private TextView text_4;
		private TextView text_5;
		private TextView text_6;
		private TextView text_7;
		private TextView text_8;
	}

}
