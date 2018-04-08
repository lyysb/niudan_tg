/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package order.listview;

import com.niudantg.admin.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XListViewFooter extends LinearLayout {
	// 加载更多
	public final static int STATE_NORMAL = 0;
	// 松开加载更多
	public final static int STATE_READY = 1;
	// 加载中
	public final static int STATE_LOADING = 2;

	// 一条数据都没有
	public final static int STATE_NULL = 3;
	// 已加载到尽头
	public final static int STATE_END = 6;
	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	private TextView mHint_null;
	private TextView mHint_end;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		mHintView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHint_null.setVisibility(View.INVISIBLE);
		mHint_end.setVisibility(View.INVISIBLE);

		if (state == STATE_READY) {
			// 松开加载更多
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			// 加载中
			mProgressBar.setVisibility(View.VISIBLE);
		} else if (state == STATE_NULL) {
			// 没有一条数据
			mHint_null.setVisibility(View.VISIBLE);

		} else if (state == STATE_END) {
			// 到了尽头
			mHint_end.setVisibility(View.VISIBLE);
		} else {
			// 加载更多
			mHintView.setVisibility(View.INVISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_normal);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		mContentView.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);

		mHint_null = (TextView) moreView
				.findViewById(R.id.xlistview_footer_null_textview);
		mHint_end = (TextView) moreView
				.findViewById(R.id.xlistview_footer_end_textview);
	}

	public void setTextNull(String text) {
		mHint_null.setText(text);
	}

	public void setTextEnd(String text) {
		mHint_end.setText(text);
	}

}
