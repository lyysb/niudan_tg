package com.niudantg.admin;

import java.io.File;
import java.util.Map;
import niudantg.Application;
import niudantg.BaseActivity;
import niudantg.Consts;
import ktx.pojo.domain.Version;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.DownLoadManager;
import com.niudantg.admin.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.layout_main)
public class MainActivity extends BaseActivity implements CFHttpMsg {
	//
	@ViewInject(R.id.btn_1)
	private LinearLayout btn_1;
	//
	@ViewInject(R.id.btn_2)
	private LinearLayout btn_2;
	//
	@ViewInject(R.id.btn_3)
	private LinearLayout btn_3;
	//
	@ViewInject(R.id.btn_4)
	private LinearLayout btn_4;
	//
	@ViewInject(R.id.btn_5)
	private RelativeLayout btn_5;
	//
	@ViewInject(R.id.btn_6)
	private RelativeLayout btn_6;
	//
	@ViewInject(R.id.btn_8)
	private RelativeLayout btn_8;
	//
	@ViewInject(R.id.btn_9)
	private RelativeLayout btn_9;
	//
	@ViewInject(R.id.btn_exit)
	private TextView btn_exit;

	//
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
		getVersion();
	}

	private void init() {
		try {
			localVersion = getVersionName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		intent = new Intent();

		if (Consts.user.BD_Status == 1) {
			btn_5.setVisibility(View.VISIBLE);
			btn_8.setVisibility(View.VISIBLE);
		} else {
			btn_5.setVisibility(View.GONE);
			btn_8.setVisibility(View.GONE);
		}
		if (Consts.user.Level == 1) {
			btn_9.setVisibility(View.VISIBLE);
		} else {
			btn_9.setVisibility(View.GONE);
		}

		if (Consts.user.Password.equals("123456")) {
			intent.setClass(MainActivity.this, UpdatePasswordActivity.class);
			startActivity(intent);
			Toast.makeText(this, "默认密码登陆，请修改密码~", Toast.LENGTH_SHORT).show();
		}

	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5,
			R.id.btn_6, R.id.btn_8, R.id.btn_9, R.id.btn_exit })
	public void viewonclick(View view) {
		switch (view.getId()) {
		//
		case R.id.btn_1:
			intent.setClass(MainActivity.this, CustomerListActivity.class);
			intent.putExtra("EId", Consts.user.id);
			startActivity(intent);
			break;
		//
		case R.id.btn_2:
			intent.setClass(MainActivity.this, SearchCarInfoActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_3:
			intent.setClass(MainActivity.this, SetCustomerInfoActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_4:
			intent.setClass(MainActivity.this, SearchPaymentBoardActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_5:
			intent.setClass(MainActivity.this, BDCarActivity.class);
			startActivity(intent);
			break;
		// //
		// case R.id.btn_6:
		// intent.setClass(MainActivity.this, OrderListActivity.class);
		// startActivity(intent);
		// break;
		//
		case R.id.btn_8:
			intent.setClass(MainActivity.this, CardBDActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_9:
			intent.setClass(MainActivity.this, ExtensionActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_exit:
			intent.setClass(MainActivity.this, SetActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	long waitTime_1 = 2000;
	long touchTime_1 = 0;

	// 再按一下退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime_1) >= waitTime_1) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				touchTime_1 = currentTime;
			} else {
				Application.exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
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

		default:
			break;
		}
	}

	public void onResume() {
		super.onResume();
		if (!Consts.tf_login) {
			intent.setClass(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
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

}
