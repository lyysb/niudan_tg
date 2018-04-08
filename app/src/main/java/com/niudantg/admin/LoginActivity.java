package com.niudantg.admin;

import java.io.File;
import java.util.Map;

import niudantg.Application;
import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.ExtensionStaff;
import ktx.pojo.domain.Version;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.DownLoadManager;
import com.niudantg.util.ToastUtil;
import com.niudantg.util.Utils;
import com.niudantg.admin.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

//注册界面
@ContentView(R.layout.layout_login)
public class LoginActivity extends BaseActivity implements CFHttpMsg {

	// 电话号码
	@ViewInject(R.id.ed_phone)
	private EditText ed_phone;
	// 验证码
	@ViewInject(R.id.ed_password)
	private EditText ed_password;
	// 登陆按钮
	@ViewInject(R.id.btn_login)
	private RelativeLayout btn_login;
	// 跳转组件
	private Intent intent;
	// 本地版本
	private String localVersion;
	/******************************************************/
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int DOWN_ERROR = 4;

	//
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_NONEED:
				// Toast.makeText(getApplicationContext(), "当前为最新版本",
				// Toast.LENGTH_SHORT).show();
				break;
			case UPDATA_CLIENT:
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1)
						.show();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
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
		intent = new Intent();
		try {
			localVersion = getVersionName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mProgressDialog.setTitle("登陆中...");
		String tg_Phone = Application.sharedpreferences.getString("tg_Phone",
				"");
		String tg_Passward = Application.sharedpreferences.getString(
				"tg_Passward", "");
		boolean klty_login = Application.sharedpreferences.getBoolean(
				"tg_login", false);

		if (tg_Phone != null && !tg_Phone.equals("")) {
			ed_phone.setText(tg_Phone);
		}

		// 登陆按钮
		btn_login.setOnClickListener(this);
		if (tg_Phone != null && !tg_Phone.equals("") && tg_Passward != null
				&& !tg_Passward.equals("")) {
			ed_phone.setText(tg_Phone);
			if (klty_login) {
				setLogin(tg_Phone, tg_Passward);
			} else {
				getVersion();
			}
		} else {
			getVersion();
		}
	}

	// 注册协议
	long waitTime = 1500;
	long touchTime = 0;

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_login })
	public void onClick(View v) {
		long currentTime = System.currentTimeMillis();
		switch (v.getId()) {

		// 登陆按钮
		case R.id.btn_login:
			if (touchTime != 0) {
				if ((currentTime - touchTime) >= waitTime) {
					touchTime = currentTime;
					if (CheckNetworkState()) {
						Setzhuceinfo();
					} else {
						ToastUtil.showMessages(LoginActivity.this,
								"网络连接失败 ,请检打开网络!");
					}
				}
			} else {
				touchTime = currentTime;
				if (CheckNetworkState()) {
					Setzhuceinfo();
				} else {
					ToastUtil.showMessages(LoginActivity.this,
							"网络连接失败 ,请检打开网络!");
				}
			}
			break;

		default:
			break;
		}
	}

	private void setLogin(String Phone, String Password) {
		getLocalMacAddress();
		mProgressDialog.show();
		Message message = new Message();
		message.what = 9002;
		CFHttpClient.s().get(
				"?MsgType=9002&mobileType=android&Phone=" + Phone
						+ "&Password=" + Password + "&DeviceID="
						+ Consts.DeviceID, this, message, false);
	}

	// 登陆
	private void Setzhuceinfo() {
		getLocalMacAddress();
		String phone1 = ed_phone.getText().toString();
		String password = ed_password.getText().toString();
		password = Utils.setSrule(password);

		if (phone1 == null || password == null || phone1.equals("")
				|| password.equals("")) {
			ToastUtil.showMessages(LoginActivity.this, "手机号码和验证码不能为空哦！");
		} else if (phone1.length() != 11) {
			ToastUtil.showMessages(LoginActivity.this, "手机号码不正确哦！");
		} else {
			mProgressDialog.show();
			Message message = new Message();
			message.what = 9002;
			CFHttpClient.s().get(
					"?MsgType=9002&mobileType=android&Phone=" + phone1
							+ "&Password=" + password + "&DeviceID="
							+ Consts.DeviceID, this, message, false);
			// System.out.println("?MsgType=7002&mobileType=android&Phone="
			// + phone1 + "&Password=" + password + "&DeviceID="
			// + Consts.DeviceID);
		}

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void httpMsg(Message m) {
		switch (m.what) {
		//
		case 9001:
			// 获取版本号
			if (m.arg1 == 1) {
				Map<String, Object> map1 = (Map<String, Object>) m.obj;
				Consts.version = (Version) map1.get("version");
				if (Consts.version.Code.equals(localVersion)) {
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			}
			break;
		// 登陆端口
		case 9002:
			mProgressDialog.cancel();
			if (m.arg1 == 1) {
				Map<String, Object> map = (Map<String, Object>) m.obj;
				Consts.user = (ExtensionStaff) map.get("user");

				Editor editor = Application.sharedpreferences.edit();
				editor.putString("tg_Phone", Consts.user.Phone);
				editor.putString("tg_Passward", Consts.user.Password);
				editor.putInt("LoginType", 1);
				editor.putBoolean("tg_login", true);
				editor.commit();
				Consts.tf_login = true;
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else if (m.arg1 == -2) {
				ToastUtil.showMessages(LoginActivity.this, "当前账号不存在");
			} else if (m.arg1 == -5) {
				ToastUtil.showMessages(LoginActivity.this, "当前账户已被后台管制，无法登陆");
			} else if (m.arg1 == -3) {
				ToastUtil.showMessages(LoginActivity.this, "密码错误，请确认后重新请求~");
			} else if (m.arg1 == -4) {
				ToastUtil.showMessages(LoginActivity.this, "账号和绑定手机不符，无法登陆");
			} else {
				ToastUtil.showMessages(LoginActivity.this, "登陆失败，请重试~");
			}
			break;

		default:
			break;
		}
	}

	public void onResume() {
		super.onResume();
	}

	// 获取版本号
	private void getVersion() {
		Message m = new Message();
		m.what = 9001;
		CFHttpClient.s().get("?MsgType=9001&mobileType=android&DeviceID=123",
				this, m, true);
	}

	private String getVersionName() throws Exception {
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	/*
	 * 
	 * 弹出对话框通知用户更新程序 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性,
	 * 对话框的内容,样式,按钮 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		String content = Consts.version.Content;
		if (content == null || content.equals("")) {
			content = String
					.format("快乐童摇推广有新版本%s了，请升级应用。", Consts.version.Code);
		}
		AlertDialog.Builder builer = new Builder(this);
		builer.setCancelable(false);
		builer.setTitle("版本升级");
		builer.setMessage(content);
		// 点击确定按钮
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downLoadApk();
			}
		});

		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(this);
		pd.setCanceledOnTouchOutside(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(
							Consts.version.Url, pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	// 获取手机mar地址
	public void getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		Consts.DeviceID = info.getMacAddress();
	}

}
