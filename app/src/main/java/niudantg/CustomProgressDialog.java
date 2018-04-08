package niudantg;

import com.niudantg.admin.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @author 设置ProgressDialog的显�?
 * 
 */

public class CustomProgressDialog extends Dialog {
	private static CustomProgressDialog customProgressDialog = null;

	public CustomProgressDialog(Context context) {
		super(context);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressDialog createDialog(Context context) {// 创建ProgressDialog
		customProgressDialog = new CustomProgressDialog(context,
				R.style.Theme_Dialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.findViewById(R.id.imageView_bg).setVisibility(
				View.GONE);
		return customProgressDialog;
	}

	public void setTitle(String title) {
		((TextView) customProgressDialog.findViewById(R.id.textView_title))
				.setText(title);
	}
}
