package niudantg.service;

import java.io.File;
import java.util.HashMap;

import niudantg.Consts;

import org.json.JSONObject;


import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.io.PutExtra;
import com.qiniu.utils.QiniuException;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class QiniuSerVice extends IntentService {

	// 图片线上的存储url
	private String redirect;
	// 图片路径存储
	private String img_file;
	// 图片路径存储
	private String img_url;;
	//
	private String token = "";
	// 回调端口
	private QiniuResult qnresult;

	private QiniuBinder qiniubinder;
	private int type;

	public QiniuSerVice() {
		// 必须实现父类的构造方法
		super("QiniuSerVice");
	}

	public IBinder onBind(Intent intent) {
		return qiniubinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		qiniubinder = new QiniuBinder();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void setIntentRedelivery(boolean enabled) {
		super.setIntentRedelivery(enabled);
	}

	protected void onHandleIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		img_file = bundle.getString("img");
		token = bundle.getString("token");
		doUpload(img_file);

	}

	/**
	 * 普通上传文件
	 */
	private void doUpload(String file_S) {
		if (file_S == null || file_S.equals("")) {
			return;
		}
		File file = new File(file_S);
		Uri uri = Uri.fromFile(file);
		String key = IO.UNDEFINED_KEY; // 自动生成key
		PutExtra extra = new PutExtra();
		extra.params = new HashMap<String, String>();
		extra.params.put("x:a", "测试中文信息");

		IO.putFile(this, token, key, uri, extra, new JSONObjectRet() {
			public void onProcess(long current, long total) {
			}

			public void onSuccess(JSONObject resp) {
				String hash = resp.optString("hash", "");
				redirect = "http://" + Consts.domain + "/" + hash;
				img_url = redirect;
				qnresult.OnQiniuResult(img_url, 1);

			}

			public void onFailure(QiniuException ex) {
				qnresult.OnQiniuResult(img_url, 2);
			}
		});
	}

	public class QiniuBinder extends Binder {

		public void addupdishback(QiniuResult qnresult, int type) {
			QiniuSerVice.this.qnresult = qnresult;
			QiniuSerVice.this.type = type;
		}

	}

}