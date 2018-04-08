package niudantg;

import java.io.File;
import java.util.ArrayList;
import com.lidroid.xutils.BitmapUtils;
import com.niudantg.admin.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Process;

public final class Application extends android.app.Application {
	// 异步加载头像
	public static BitmapUtils mBU_Head;

	// 保存用户信息的配置文件
	public static SharedPreferences sharedpreferences;
	/**
	 * sd卡的根目录
	 **/
	private static String mSdRootPath = Environment
			.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	private static ArrayList<Activity> mActivityList = new ArrayList<Activity>();

	public static File file = null;

	public void onCreate() {
		getLocalMacAddress();
		Consts.BasePath = getStorageDirectory();

		sharedpreferences = this.getSharedPreferences("nd_tg", MODE_PRIVATE);

		mBU_Head = new BitmapUtils(this, Consts.BasePath1 + Consts.BasePath2);
		mBU_Head.configDefaultLoadingImage(R.drawable.empty_photo);

		try {

			File file = new File(Consts.BasePath + Consts.BasePath1);
			if (!file.exists()) {
				try {
					// 创建目录
					file.mkdirs();
				} catch (Exception e) {

				}
			}
			// File file2 = new File(Consts.BasePath1 + Consts.BasePath2);
			// if (!file2.exists()) {
			// try {
			// // 创建目录
			// file2.mkdirs();
			// } catch (Exception e) {
			//
			// }
			// }

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 获取储存Image的目录
	 * 
	 * @return
	 */
	private String getStorageDirectory() {
		mDataRootPath = this.getCacheDir().getPath();
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? mSdRootPath : mDataRootPath;
	}

	public static void addActivity(Activity activity) {
		mActivityList.add(activity);
	}

	public static void removeActivity(Activity activity) {
		mActivityList.remove(activity);
	}

	public static void exit() {
		for (Activity activity : mActivityList) {
			activity.finish();
		}
		Process.killProcess(Process.myPid());
	}

	public static ArrayList<Activity> getActivityList() {
		return mActivityList;
	}

	// 获取手机mar地址
	public void getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		Consts.DeviceID = info.getMacAddress();
	}

}
