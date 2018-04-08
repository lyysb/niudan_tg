package com.niudantg.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.adapter.CarInfo_Adapter;
import com.niudantg.adapter.Customer_Adapter;
import com.niudantg.adapter.EggListAdapter;
import com.niudantg.adapter.Region_Adapter;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpClient_LYY;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ResultInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ktx.pojo.domain.Customer;
import ktx.pojo.domain.EggReward;
import ktx.pojo.domain.EquipmentEggInfo;
import ktx.pojo.domain.EquipmentInfo;
import ktx.pojo.domain.RegionInfo;
import niudantg.BaseActivity;
import niudantg.Consts;
import order.listview.XListView;

/**
 * Created by LYY on 2018/4/3.
 */
@ContentView(R.layout.layout_eggs)
public class EggshellActivity extends BaseActivity implements CFHttpMsg, XListView.IXListViewListener {

    //
    @ViewInject(R.id.egg_id)
    private TextView egg_id;
    @ViewInject(R.id.egg_shop)
    private TextView egg_shop;
    //
    @ViewInject(R.id.egg_num)
    private TextView egg_num;
    @ViewInject(R.id.egg_daty)
    private TextView egg_daty;


    //

    @ViewInject(R.id.egg_aout)
    private TextView egg_aout;
    @ViewInject(R.id.egg_dout)
    private TextView egg_dout;
    @ViewInject(R.id.egg_save)
    private TextView egg_save;


    //
    @ViewInject(R.id.btn_lay1)
    private RelativeLayout btn_lay1;
    @ViewInject(R.id.btn_lay2)
    private RelativeLayout btn_lay2;
    //
    @ViewInject(R.id.img_lay1)
    private ImageView img_lay1;
    @ViewInject(R.id.img_lay2)
    private ImageView img_lay2;
    //submit
    @ViewInject(R.id.submit)
    private TextView submit;
    @ViewInject(R.id.edit1)
    private TextView edit1;
    @ViewInject(R.id.edit2)
    private TextView edit2;
    //
    @ViewInject(R.id.egg_list)
    private XListView egg_list;
    //
    private Intent intent;
    private int position;
    // 记录信息条数
    private int order_num = 0;
    // 记录拖拉状态
    private int tl_status = XListView.REFRESH;
    // 检索控制
    private boolean tf = true;
    private int EquipmentId;

    private int CustomerId;




    private EquipmentEggInfo equipmentegginfo = new EquipmentEggInfo();
    private ArrayList<EggReward> datalist = new ArrayList<EggReward>();
    private EggListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
    }

    private void init() {
        EquipmentId = this.getIntent().getIntExtra("EquipmentId", 0);
        CustomerId = this.getIntent().getIntExtra("CustomerId", 0);
        intent = new Intent();
        // 订单listview
        egg_list.setPullLoadEnable(true);
        egg_list.setPullRefreshEnable(false);
        egg_list.setXListViewListener(this);
        egg_list.mHeaderView.setHeader(60);

        egg_list.mFooterView.setTextNull("还没有记录~");
        egg_list.mFooterView.setTextEnd("已是最后的记录了~");
        getEggRecode();
        adapter = new EggListAdapter(this, datalist);
        egg_list.setAdapter(adapter);
//赋值
        egg_id.setText(String.format("ID:%d",CustomerId));
        //商定名字
        if (equipmentegginfo.OpenBankName != null || !"".equals(equipmentegginfo.OpenBankName)) {
            egg_shop.setText(equipmentegginfo.OpenBankName);
        }


        //
        egg_num.setText(String.format("%d",EquipmentId));
        egg_aout.setText(String.format("%d", equipmentegginfo.AllEggNum));
        egg_dout.setText(String.format("%d", equipmentegginfo.AverageEggNum));
        egg_save.setText(String.format("%d", equipmentegginfo.CurrentEggNum));
    }


    // 下方控制栏的点击事件
    @OnClick({R.id.btn_back, R.id.btn_lay1, R.id.btn_lay2, R.id.submit})
    public void viewonclick(View v) {
        switch (v.getId()) {
            //
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_lay1:

                img_lay1.setVisibility(View.VISIBLE);
                img_lay2.setVisibility(View.GONE);

//                adapter = new EggListAdapter(this, datalist);
//                egg_list.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_lay2:

                img_lay2.setVisibility(View.VISIBLE);
                img_lay1.setVisibility(View.GONE);

                break;

            case R.id.submit:
                getEggList();
                if (datalist.get(position).Content != null && !datalist.get(position).Content.equals("")) {
                    edit1.setText(datalist.get(position).Content);
                } else {
                    edit1.setText("蛋壳内容,不能为空");
                }
                edit2.setText(String.format("%d", datalist.get(position).Num));
                adapter = new EggListAdapter(this, datalist);
                egg_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }


    private void getEggRecode() {

        mProgressDialog.setTitle("加载中");
        mProgressDialog.show();
        Message m = new Message();
        m.what = 9026;
        CFHttpClient.s().get(
                "?MsgType=9026&mobileType=android&EquipmentId="
                        + EquipmentId + "&CustomerId=" + CustomerId + "&Index=" + order_num,this, m, true);

    }

    private void getEggList() {
        mProgressDialog.setTitle("加载中");
        mProgressDialog.show();
        Message m = new Message();
        m.what = 9027;
        CFHttpClient.s().get(
                "?MsgType=9027&mobileType=android&EquipmentId=" + EquipmentId,
                this, m, true);

    }


    @Override
    public void httpMsg(Message m) {
        switch (m.what) {
            //
            case 9026:
                mProgressDialog.cancel();
                tf = true;
                if (m.arg1 == 1) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) m.obj;
                    @SuppressWarnings("unchecked")
                    ArrayList<EggReward> result = (ArrayList<EggReward>) map
                            .get("datalist");
                    if (adapter == null) {
                        adapter = new EggListAdapter(this, datalist);
                        egg_list.setAdapter(adapter);
                    }
                    if (tl_status == XListView.REFRESH) {
                   //    egg_list.clear();
                      //egg_list.addAll(result);
                    } else {
                       //egg_list.addAll(result);
                    }
                    adapter.setNewListInfo(datalist);
                    adapter.notifyDataSetChanged();
                    order_num = datalist.size();
                    egg_list.stopRefresh();
                    egg_list.stopLoadMore();
                    egg_list.setResultSize(result.size(), datalist.size());
                } else {
                    Toast.makeText(EggshellActivity.this, "检索失败~",
                            Toast.LENGTH_SHORT).show();
                    egg_list.stopRefresh();
                    egg_list.stopLoadMore();
                }
                break;
            case 9027:
                if (m.arg1 == 1) {
                    getEggList();
                    Toast.makeText(EggshellActivity.this, "提交成功~",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EggshellActivity.this, "提交失败~",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void loadData(final int what) {
        tl_status = what;
        getEggRecode();

    }

    @Override
    public void onRefresh() {
        if (tf) {
            order_num = 0;
            loadData(XListView.REFRESH);
            tf = false;
        }
    }

    @Override
    public void onLoadMore() {
        if (tf) {
            loadData(XListView.LOAD);
            tf = false;
        }
    }
}
