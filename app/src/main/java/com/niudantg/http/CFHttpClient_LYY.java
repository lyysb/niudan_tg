package com.niudantg.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import ktx.pojo.domain.EquipmentInfo;

import org.json.JSONException;
import com.alibaba.fastjson.JSON;
import com.niudantg.util.Md5;

public class CFHttpClient_LYY {

	public final static String api_key = "aa914648d183d06c";
	public final static String secret_key = "ad54d59f42bd896068bb5c2db05bad79";

	private final static String HTTP_URL = "https://m.leyaoyao.com/lyy/rest/lyy/devicesStatus";
	//
	private static String sign;
	//
	private static ArrayList<String> Code = new ArrayList<String>();
	//
	private static String str;
	private static String str1;

	public static String setData(ArrayList<EquipmentInfo> datalist)
			throws JSONException {
		for (EquipmentInfo car : datalist) {
			if (car.CType == 1) {
				Code.add(car.DeviceID);
			}
		}
		str = URLEncoder.encode(JSON.toJSONString(Code));
		str1 = JSON.toJSONString(Code);
		Md5 md5 = new Md5();
		String paramString = String.format("%s%s%s",
				"api_keyaa914648d183d06cdevices", str1, secret_key);
		try {
			sign = md5.getmd5(paramString.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 多设备查询
		String Url = String.format("%s?api_key=%s&sign=%s&devices=%s",
				HTTP_URL, api_key, sign, str);
		String result = sendGetRequest(Url);

		return result;
	}

	public static String sendGetRequest(String getUrl) throws JSONException {
		StringBuffer sb = new StringBuffer();
		// InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			URL url = new URL(getUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			// isr = new InputStreamReader(url.openStream());
			// br = new BufferedReader(isr);
			br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "utf-8"));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				// if (isr != null)
				// isr.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
