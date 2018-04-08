package com.niudantg.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public final class ToastUtil {
	private static Handler handler = new Handler(Looper.getMainLooper());
	private static Toast toast = null;
	private static Object synObj = new Object();

	public static void showMessage(final Context act, final String msg_content) {
		showMessages(act, msg_content);
	}

	public static void showMessages(final Context act, final String msg) {
		if (toast != null) {
			toast.setText(msg);
		} else {
			toast = Toast.makeText(act, msg, Toast.LENGTH_LONG);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
