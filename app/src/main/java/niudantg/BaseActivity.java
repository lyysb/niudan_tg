package niudantg;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseActivity extends FragmentActivity implements OnClickListener {
	public boolean isShowToast = false;
	protected Handler mHandler;
	// 缓冲界面
	public CustomProgressDialog mProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Application.addActivity(this);
		if (mHandler == null) {
			mHandler = new Handler();
		}
		if (mProgressDialog == null) {
			mProgressDialog = CustomProgressDialog.createDialog(this);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onRestart() {

		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		Application.removeActivity(this);
		super.onDestroy();
	}

	public void onClick(View v) {
	}

	// 是否联网
	public boolean CheckNetworkState() {

		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

}
