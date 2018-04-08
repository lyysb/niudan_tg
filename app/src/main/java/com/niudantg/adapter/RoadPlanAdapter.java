package com.niudantg.adapter;

import java.util.List;

import com.tencent.lbssearch.object.result.DrivingResultObject;
import com.tencent.lbssearch.object.result.RoutePlanningObject;
import com.tencent.lbssearch.object.result.TransitResultObject;
import com.tencent.lbssearch.object.result.TransitResultObject.Line;
import com.tencent.lbssearch.object.result.TransitResultObject.Segment;
import com.tencent.lbssearch.object.result.TransitResultObject.Transit;
import com.tencent.lbssearch.object.result.WalkingResultObject;
import com.niudantg.admin.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoadPlanAdapter extends BaseAdapter {

	private Context mContext;
	private List<WalkingResultObject.Route> walkRoutes;
	private List<DrivingResultObject.Route> driveRoutes;
	private List<TransitResultObject.Route> transitRoutes;

	// private List<TricycleResultObject.Route> tricycleRoutes;

	public RoadPlanAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void setPlanObject(RoutePlanningObject obj) {
		walkRoutes = null;
		driveRoutes = null;
		transitRoutes = null;
		// tricycleRoutes = null;
		if (obj instanceof WalkingResultObject) {
			WalkingResultObject walkObj = (WalkingResultObject) obj;
			walkRoutes = walkObj.result.routes;
		} else if (obj instanceof DrivingResultObject) {
			DrivingResultObject drivingObj = (DrivingResultObject) obj;
			driveRoutes = drivingObj.result.routes;
		} else if (obj instanceof TransitResultObject) {
			TransitResultObject transitObj = (TransitResultObject) obj;
			transitRoutes = transitObj.result.routes;
		} /*
		 * else if (obj instanceof TricycleResultObject) { TricycleResultObject
		 * tricycleObj = (TricycleResultObject) obj; tricycleRoutes =
		 * tricycleObj.result.routes; }
		 */
	}

	public List<WalkingResultObject.Route> getWalkRoutes() {
		return walkRoutes;
	}

	public List<DrivingResultObject.Route> getDriveRoutes() {
		return driveRoutes;
	}

	public List<TransitResultObject.Route> getTransitRoutes() {
		return transitRoutes;
	}

	// public List<TricycleResultObject.Route> getTricycleRoutes() {
	// return tricycleRoutes;
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		if (walkRoutes != null) {
			count = walkRoutes.size();
		}
		if (driveRoutes != null) {
			count = driveRoutes.size();
		}
		if (transitRoutes != null) {
			count = transitRoutes.size();
		}
		// if (tricycleRoutes != null) {
		// count = tricycleRoutes.size();
		// }
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Object obj = null;
		if (walkRoutes != null) {
			obj = walkRoutes.get(position);
		}
		if (driveRoutes != null) {
			obj = driveRoutes.get(position);
		}
		if (transitRoutes != null) {
			obj = transitRoutes.get(position);
		}
		// if (tricycleRoutes != null) {
		// obj = tricycleRoutes.get(position);
		// }
		return obj;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.suggestion_list_item,
					null);
			viewHolder.label = (TextView) convertView.findViewById(R.id.label);
			viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String strLabel = "";
		String strDesc = "";
		if (walkRoutes != null) {
			WalkingResultObject.Route route = walkRoutes.get(position);
			strLabel = new String("距离：" + getDistance(route.distance)
					+ ", 预计用时：" + getDuration(route.duration));
			/*
			 * RoutePlanningObject.Step 每段路线的详细信息 附加描述 accessorial_desc 如“进入主路”
			 * 动作描述 act_desc 方向 dir_desc 距离 distance 用时 duration 默认提供的路段说明
			 * instruction 路段点串在总路线点串中的位置 polyline_idx(起点和终点) 路名 road_name
			 */
			if (route.steps != null && route.steps.size() > 0) {
				strDesc = new String(route.steps.get(0).instruction + "... ...");
			} else {
				strDesc = new String("暂无详情");
			}
		}
		if (driveRoutes != null) {
			DrivingResultObject.Route route = driveRoutes.get(position);
			strLabel = new String("距离：" + getDistance(route.distance)
					+ ", 预计用时：" + getDuration(route.duration));
			if (route.steps != null && route.steps.size() > 0) {
				strDesc = new String(route.steps.get(0).instruction + "... ...");
			} else {
				strDesc = new String("暂无详情");
			}
		}
		if (transitRoutes != null) {
			TransitResultObject.Route route = transitRoutes.get(position);
			strLabel = new String("距离：" + getDistance(route.distance)
					+ "，预计用时：" + getDuration(route.duration));
			/*
			 * TransitResultObject.Segment是TransitResultObject.Walking和
			 * TransitResultObject.Transit的父类
			 * 其中TransitResultObject.Walking的结构基本与WalkingResultObject.Route相同
			 * TransitResultObject.Transit中包含lines字段，只有lines的第一个元素才会有路线点串
			 */
			List<Segment> segments = route.steps;
			if (segments == null) {
				strDesc = new String("暂无详情");
			}
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < segments.size(); i++) {
				Segment segment = segments.get(i);
				if (segment instanceof Transit) {
					List<Line> lines = ((Transit) segment).lines;
					if (lines == null) {
						return null;
					}
					stringBuilder.append(lines.get(0).title);
					if (lines.size() > 1) {
						for (int j = 1; j < lines.size(); j++) {
							stringBuilder.append("/" + lines.get(j).title);
						}
					}
				}
				if (i != 0 && i < segments.size() - 1
						&& segments.get(i + 1) instanceof Transit) {
					stringBuilder.append(" -> ");
				}
			}
			strDesc = stringBuilder.toString();
		}
		// if (tricycleRoutes != null) {
		// TricycleResultObject.Route route = tricycleRoutes.get(position);
		// strLabel = new String("距离：" + getDistance(route.distance) +
		// ", 预计用时：" + getDuration(route.duration));
		// if (route.steps != null && route.steps.size() > 0) {
		// if (route.steps.get(0).instruction == null) {
		// strDesc = new String("暂无详情");
		// } else {
		// strDesc = new String(route.steps.get(0).instruction + "... ...");
		// }
		// } else {
		// strDesc = new String("暂无详情");
		// }
		// }
		viewHolder.label.setText(strLabel);
		viewHolder.desc.setText(strDesc);
		return convertView;
	}

	class ViewHolder {
		TextView label;
		TextView desc;
	}

	/**
	 * 将距离转换成米或千米
	 * 
	 * @param distance
	 * @return
	 */
	protected String getDistance(float distance) {
		if (distance < 1000) {
			return Integer.toString((int) distance) + "米";
		} else {
			return Float.toString((float) ((int) (distance / 10)) / 100) + "千米";
		}
	}

	/**
	 * 将时间转换成小时+分钟
	 * 
	 * @param duration
	 * @return
	 */
	protected String getDuration(float duration) {
		if (duration < 60) {
			return Integer.toString((int) duration) + "分钟";
		} else {
			return Integer.toString((int) (duration / 60)) + "小时"
					+ Integer.toString((int) (duration % 60)) + "分钟";
		}
	}
}
