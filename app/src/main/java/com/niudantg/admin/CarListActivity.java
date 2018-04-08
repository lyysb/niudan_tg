package com.niudantg.admin;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import ktx.pojo.domain.EquipmentInfo;
import niudantg.BaseActivity;
import niudantg.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.adapter.CarInfo_Adapter;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpClient_LYY;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ResultInfo;
import com.niudantg.admin.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_carlist)
public class CarListActivity extends BaseActivity implements CFHttpMsg {

    @ViewInject(R.id.t_title)
    private TextView t_title;
    // 返回按钮
    @ViewInject(R.id.btn_back)
    private ImageView btn_back;
    // 返回按钮
    @ViewInject(R.id.btn_addcar)
    private TextView btn_addcar;
    // 返回按钮
    @ViewInject(R.id.btn_alljb)
    private TextView btn_alljb;

    //
    @ViewInject(R.id.list_car)
    private ListView list_car;
    //
    @ViewInject(R.id.t_freedata)
    private TextView t_freedata;
    //
    private int CustomerId;

    // 列表适配器
    private CarInfo_Adapter adapter;
    // 收入列表
    private ArrayList<EquipmentInfo> datalist = new ArrayList<EquipmentInfo>();
    //
    private Intent intent;
    //
    private int position;
    //
    private Bundle bundle = null;
    //
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 修改价格
                case 3:

                    position = msg.arg1;
                    if (datalist.get(position).CustomerId != 0) {
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        intent.setClass(CarListActivity.this,
                                UpdatePriceActivity.class);
                        bundle.putString("CarName", datalist.get(position).Name);
                        bundle.putFloat("Price", datalist.get(position).Price);
                        bundle.putInt("CarId", datalist.get(position).id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    break;
                // 解绑
                case 6:

                    position = msg.arg1;
                    if (datalist.get(position).CustomerId != 0) {
                        setQuerenDialog2(datalist.get(position).DeviceID, position);
                    }
                    break;
                // 解绑
                case 9:

                    position = msg.arg1;
                    if (datalist.get(position).CustomerId != 0) {
                        intent.setClass(CarListActivity.this,
                                EggshellActivity.class);
                        intent.putExtra("EquipmentId", datalist.get(position).id);
                        intent.putExtra("CustomerId", datalist.get(position).CustomerId);
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
        if (Consts.user.BD_Status == 1) {
            btn_addcar.setVisibility(View.VISIBLE);
            t_title.setVisibility(View.GONE);
        } else {
            btn_addcar.setVisibility(View.GONE);
            t_title.setVisibility(View.VISIBLE);
        }
        intent = new Intent();
        CustomerId = this.getIntent().getIntExtra("CustomerId", 0);
        adapter = new CarInfo_Adapter(this, datalist, handler);
        list_car.setAdapter(adapter);

        getCarList();

    }

    // 下方控制栏的点击事件
    @OnClick({R.id.btn_back, R.id.btn_addcar, R.id.btn_alljb})
    public void viewonclick(View v) {
        switch (v.getId()) {
            //
            case R.id.btn_back:
                finish();
                break;
            //
            case R.id.btn_addcar:
                intent.setClass(CarListActivity.this, AddCarActivity.class);
                startActivity(intent);
                break;
            //
            case R.id.btn_alljb:
                setQuerenDialog();
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
        m.what = 9005;
        CFHttpClient.s().get(
                "?MsgType=9005&mobileType=android&CustomerId=" + CustomerId,
                this, m, true);

    }

    public void httpMsg(Message m) {
        switch (m.what) {
            //
            case 9005:
                if (m.arg1 == 1) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) m.obj;
                    datalist = (ArrayList<EquipmentInfo>) map.get("eqlist");
                    //
                    if (Consts.user.BD_Status == 1 && datalist != null
                            && datalist.size() != 0) {
                        btn_alljb.setVisibility(View.VISIBLE);
                    } else {
                        btn_alljb.setVisibility(View.GONE);
                    }
                    //
                    long currentTime = System.currentTimeMillis();
                    if (datalist != null && datalist.size() != 0) {
                        String result;
                        try {
                            result = CFHttpClient_LYY.setData(datalist);
                            if (result != null && !result.equals("")) {
                                JSONObject jo = new JSONObject(result);
                                int result1 = jo.getInt("result");

                                if (result1 == 0) {
                                    ArrayList<ResultInfo> resultlist = (ArrayList<ResultInfo>) JSON
                                            .parseArray(jo.getString("data"),
                                                    ResultInfo.class);
                                    for (EquipmentInfo car : datalist) {
                                        for (int i = 0; i < resultlist.size(); i++) {

                                            int result2 = resultlist.get(i).result;
                                            String device_info = resultlist.get(i).device_info;
                                            String description = resultlist.get(i).description;
                                            if (car.DeviceID.equals(device_info)) {
                                                // savetosdcard(car.DeviceID + "---"
                                                // + car.Name + "---"
                                                // + description, "" + currentTime);
                                                car.MallName = description;
                                                car.Status = result2;
                                            }
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    mProgressDialog.cancel();
                    if (adapter == null) {
                        adapter = new CarInfo_Adapter(this, datalist, handler);
                        list_car.setAdapter(adapter);
                    } else {
                        adapter.setNewListInfo(datalist);
                        adapter.notifyDataSetChanged();
                    }

                    t_freedata.setVisibility(View.INVISIBLE);
                    list_car.setVisibility(View.VISIBLE);

                } else if (m.arg1 == -2) {
                    mProgressDialog.cancel();
                    btn_alljb.setVisibility(View.GONE);
                    t_freedata.setVisibility(View.VISIBLE);
                    list_car.setVisibility(View.INVISIBLE);
                } else {
                    mProgressDialog.cancel();
                    Toast.makeText(CarListActivity.this, "检索失败~",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            //
            case 9011:
                mProgressDialog.cancel();
                if (m.arg1 == 1) {
                    getCarList();
                    Toast.makeText(CarListActivity.this, "解绑成功~",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarListActivity.this, "请求提交失败~",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            //
            case 9016:
                if (m.arg1 == 1) {
                    getCarList();
                    Toast.makeText(CarListActivity.this, "解绑成功~",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarListActivity.this, "提交失败~",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Consts.addcar_tf) {
            Consts.addcar_tf = false;
            getCarList();
        }
        if (Consts.updateprice_tf) {
            Consts.updateprice_tf = false;
            getCarList();
        }
    }

    /* 将调试信息保存到sdcard上 */
    private void savetosdcard(String s, String name) {
        try {
            FileOutputStream outStream = new FileOutputStream(Consts.BasePath
                    + Consts.BasePath1 + "/" + name + ".txt", true);
            OutputStreamWriter writer = new OutputStreamWriter(outStream,
                    "gb2312");
            writer.write(s);
            writer.write("/n");
            writer.flush();
            writer.close();// 记得关闭
            outStream.close();
        } catch (Exception e) {
        }
    }

    // 确定界面
    protected void setQuerenDialog() {

        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(false);
        Window window = dlg.getWindow();

        window.setContentView(R.layout.dialog_queren);

        // title
        TextView t_title = (TextView) window.findViewById(R.id.text_title);
        t_title.setText("确定解绑全部设备吗？");
        // 确定按钮
        RelativeLayout btn_dialog_ok = (RelativeLayout) window
                .findViewById(R.id.btn_dialog_ok);
        // 取消按钮
        RelativeLayout btn_dialog_cancel = (RelativeLayout) window
                .findViewById(R.id.btn_dialog_cancel);

        btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getJBCarList();
                dlg.cancel();
            }
        });

        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });

    }

    // 解绑所有设备
    private void getJBCarList() {
        mProgressDialog.setTitle("提交中");
        mProgressDialog.show();
        String str = URLEncoder.encode(JSON.toJSONString(datalist));
        Message m = new Message();
        m.what = 9011;
        CFHttpClient.s().get("?MsgType=9011&mobileType=android&carlist=" + str,
                this, m, true);

    }

    // 解绑一台设备
    private void setCarInfoStatus(int position) {

        mProgressDialog.setTitle("提交中");
        mProgressDialog.show();
        Message m = new Message();
        m.what = 9016;
        CFHttpClient.s().get(
                "?MsgType=9016&mobileType=android&CarId="
                        + datalist.get(position).id + "&CustomerId="
                        + datalist.get(position).CustomerId + "&EId="
                        + Consts.user.id, this, m, true);

    }

    // 确定界面
    protected void setQuerenDialog2(String CarId, final int position) {

        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(false);
        Window window = dlg.getWindow();

        window.setContentView(R.layout.dialog_queren);

        // title
        TextView t_title = (TextView) window.findViewById(R.id.text_title);
        t_title.setText(String.format("确定解绑(编号:%s)设备吗？", CarId));
        // 确定按钮
        RelativeLayout btn_dialog_ok = (RelativeLayout) window
                .findViewById(R.id.btn_dialog_ok);
        // 取消按钮
        RelativeLayout btn_dialog_cancel = (RelativeLayout) window
                .findViewById(R.id.btn_dialog_cancel);

        btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCarInfoStatus(position);
                dlg.cancel();
            }
        });

        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });

    }

}
