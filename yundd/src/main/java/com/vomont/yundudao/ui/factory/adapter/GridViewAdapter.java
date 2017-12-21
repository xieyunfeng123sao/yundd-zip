package com.vomont.yundudao.ui.factory.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 设备列表的适配
 * 
 * 
 * @author Administrator xyf
 * 
 */
@SuppressLint("InflateParams")
public class GridViewAdapter extends BaseAdapter {
	private List<DeviceInfo> mlist;

	private Context context;

	public GridViewAdapter(List<DeviceInfo> mlist, Context context) {
		this.context = context;
		this.mlist = mlist;
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_gridveiw, null);
			holder.device_statue = (LinearLayout) convertView
					.findViewById(R.id.device_statue);
			holder.device_name = (TextView) convertView
					.findViewById(R.id.device_name);
			holder.device_privilege = (ImageView) convertView
					.findViewById(R.id.device_privilege);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.device_name.setText(mlist.get(position).getDevicename());

		if (mlist.get(position).getOnline()== 0) {
			holder.device_statue
					.setBackgroundResource(R.drawable.gridview_linearlayout_pressed);
		} else {
			holder.device_statue
					.setBackgroundResource(R.drawable.gridview_linearlayout);
		}
		if (mlist.get(position).getHasright()== 0) {
			holder.device_privilege.setVisibility(View.VISIBLE);
		} else {
			holder.device_privilege.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	class Holder {
		LinearLayout device_statue;

		TextView device_name;

		ImageView device_privilege;
	}
}
