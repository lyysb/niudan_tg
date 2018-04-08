package com.niudantg.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ktx.pojo.domain.Customer;
import ktx.pojo.domain.CustomerType;
import ktx.pojo.domain.EggReward;
import ktx.pojo.domain.EquipmentEggInfo;
import ktx.pojo.domain.EquipmentInfo;
import ktx.pojo.domain.ExtensionStaff;
import ktx.pojo.domain.OrderInfo3;
import ktx.pojo.domain.RegionInfo;
import ktx.pojo.domain.StatisticsInfo;
import ktx.pojo.domain.Version;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class CFHttpClient {

    // private final String HTTP_URL =
    // "http://10.165.106.84:8080/niudan-server/coreServlet";
//10.165.106.210
    private final String HTTP_URL = "http://101.200.85.188:8080/niudan-server/coreServlet";

    private CFHttpClient() {
    }

    private static CFHttpClient httpClient = new CFHttpClient();

    public static CFHttpClient s() {
        return httpClient;
    }

    public void get(final String url, final CFHttpMsg httpMsg,
                    final Message msg, final boolean tf) {
        new Thread() {
            public void run() {
                Boolean isOne = false;
                try {
                    StringBuffer sb = new StringBuffer();

                    BasicHttpParams http = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(http, 40000);
                    HttpConnectionParams.setSoTimeout(http, 40000);
                    HttpClient client = new DefaultHttpClient(http);

                    String httpUrl = String.format("%1$s%2$s", HTTP_URL, url);
                    HttpGet get = new HttpGet(httpUrl);

                    HttpResponse response = client.execute(get);

                    int code = response.getStatusLine().getStatusCode();

                    if (200 == code) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(response.getEntity()
                                        .getContent()));

                        String line = "";
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();

                        if (tf) {
                            Thread.sleep(600);
                        }

                        switch (msg.what) {
                            case 8000:
                                handler.sendMessage(send8000(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9001:
                                handler.sendMessage(send9001(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9002:
                                handler.sendMessage(send9002(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9003:
                                handler.sendMessage(send9003(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9004:
                                handler.sendMessage(send9004(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9005:
                                handler.sendMessage(send9005(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9006:
                                handler.sendMessage(send9006(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9007:
                                handler.sendMessage(send9007(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9008:
                                handler.sendMessage(send9008(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;

                            case 9009:
                                handler.sendMessage(send9009(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;

                            case 9010:
                                handler.sendMessage(send9010(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9011:
                                handler.sendMessage(send9011(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9012:
                                handler.sendMessage(send9012(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9013:
                                handler.sendMessage(send9013(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9014:
                                handler.sendMessage(send9014(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9015:
                                handler.sendMessage(send9015(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9016:
                                handler.sendMessage(send9016(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9017:
                                handler.sendMessage(send9017(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9018:
                                handler.sendMessage(send9018(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9019:
                                handler.sendMessage(send9019(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9024:
                                handler.sendMessage(send9024(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9025:
                                handler.sendMessage(send9025(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9026:
                                handler.sendMessage(send9026(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                            case 9027:
                                handler.sendMessage(send9027(msg, new CFMessage(
                                        httpMsg, sb, null)));
                                break;
                        }
                        isOne = true;
                    }
                } catch (Exception e) {
                }
                if (!isOne) {
                    CFMessage cfm = new CFMessage(httpMsg, null, null);
                    msg.obj = cfm;
                    msg.arg1 = -1;
                    handlerError.sendMessage(msg);
                }
            }

            ;
        }.start();
    }

    private Handler handlerError = new Handler() {
        public void handleMessage(Message msg) {
            CFMessage message = (CFMessage) msg.obj;
            CFHttpMsg httpmsg = message.getHttpMsg();
            msg.obj = null;
            httpmsg.httpMsg(msg);
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            CFMessage message = (CFMessage) msg.obj;
            CFHttpMsg httpmsg = message.getHttpMsg();
            msg.obj = message.getObject();
            httpmsg.httpMsg(msg);
        }
    };

    // token接口8000
    private Message send8000(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uptoken", jo.getString("uptoken"));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 获取版本9001
    private Message send9001(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("version",
                JSON.parseObject(jo.getString("version"), Version.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 登陆注册端口9002
    private Message send9002(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user",
                JSON.parseObject(jo.getString("user"), ExtensionStaff.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 获取商家端口9003
    private Message send9003(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customer",
                JSON.parseObject(jo.getString("customer"), Customer.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 获取商户信息端口9004
    private Message send9004(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customer",
                JSON.parseObject(jo.getString("customer"), Customer.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 获取商家绑定摇摇车端口9005
    private Message send9005(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eqlist",
                JSON.parseArray(jo.getString("eqlist"), EquipmentInfo.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 设备绑定端口9006
    private Message send9006(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 获取店铺分类端口9007
    private Message send9007(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customertypelist", JSON.parseArray(
                jo.getString("customertypelist"), CustomerType.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 获取绑定商家端口9008
    private Message send9008(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerlist",
                JSON.parseArray(jo.getString("customerlist"), Customer.class));
        map.put("customernum", jo.getInt("customernum"));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 上传推广信息端口9009
    private Message send9009(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 绑定摇摇车辨析端口9010
    private Message send9010(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eqinfo",
                JSON.parseObject(jo.getString("eqinfo"), EquipmentInfo.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 解绑设备信息端口9011
    private Message send9011(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 修改摇摇车价格端口9012
    private Message send9012(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 获取区域信息端口9013
    private Message send9013(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("regionlist",
                JSON.parseArray(jo.getString("regionlist"), RegionInfo.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 获取订单list端口9014
    private Message send9014(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderlist",
                JSON.parseArray(jo.getString("orderlist"), OrderInfo3.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 支付模块绑定解绑端口9015
    private Message send9015(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 解绑端口9016
    private Message send9016(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 获取统计信息端口9017
    private Message send9017(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statisticsinfo", JSON.parseObject(
                jo.getString("statisticsinfo"), StatisticsInfo.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 修改电话端口9018
    private Message send9018(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 修改密码端口9019
    private Message send9019(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 获取下属信息端口9024
    private Message send9024(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("EsList",
                JSON.parseArray(jo.getString("EsList"), ExtensionStaff.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }

    // 上传推广员信息端口9025
    private Message send9025(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }

    // 扭蛋管理9026
    private Message send9026(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("datalist",
                JSON.parseArray(jo.getString("datalist"), EggReward.class));
        map.put("equipmentegginfo",
                JSON.parseObject(jo.getString("equipmentegginfo"), EquipmentEggInfo.class));
        cfm.setObject(map);
        msg.obj = cfm;
        return msg;
    }
    //扭蛋记录详情

    private Message send9027(Message msg, CFMessage cfm) throws Exception {
        JSONObject jo = new JSONObject(cfm.getSb().toString());
        msg.arg1 = jo.getInt("result");
        msg.obj = cfm;
        return msg;
    }
}
