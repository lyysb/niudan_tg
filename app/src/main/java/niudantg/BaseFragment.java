package niudantg;


import com.lidroid.xutils.ViewUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment  extends Fragment{
	private Context context;
	
	public void  onAttach(Activity activity){
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutId(),container,false);
		ViewUtils.inject(this,view);
		initParams();
		return view;
		
	}

	//初始化布局
	protected abstract int getLayoutId();
	//参数设置
	protected abstract void initParams();
	


}
