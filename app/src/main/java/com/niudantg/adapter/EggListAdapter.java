package com.niudantg.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.niudantg.admin.R;

import java.util.ArrayList;

import ktx.pojo.domain.EggReward;

/**
 * Created by LYY on 2018/4/4.
 */

public class EggListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EggReward> datalist;
//    private Handler handler;


    public EggListAdapter(Context context, ArrayList<EggReward> datalist) {
        this.context = context;
        this.datalist = datalist;
        //this.handler=handler;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (convertView == null)

        {
            convertView = View.inflate(context, R.layout.lay_eggrecord, null);
            holder = new ViewHolder();
            holder.egg_name = (TextView) convertView.findViewById(R.id.egg_name);
            holder.egg_date = (TextView) convertView.findViewById(R.id.egg_date);
            holder.egg_tou = (TextView) convertView.findViewById(R.id.egg_tou);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        String content = datalist.get(position).Content;
        String createTime = datalist.get(position).CreateTime;
        int num = datalist.get(position).Num;
        if (content != null || !"".equals(content)) {
            holder.egg_name.setText(content);
        }
        if (createTime != null || !"".equals(createTime)) {
            holder.egg_date.setText(createTime);
        }
        holder.egg_tou.setText(num);

        return convertView;
    }

    public void setNewListInfo(ArrayList<EggReward> datallist) {

        this.datalist=datallist;

    }

    class ViewHolder {
        private TextView egg_name;
        private TextView egg_date;
        private TextView egg_tou;

    }
}
