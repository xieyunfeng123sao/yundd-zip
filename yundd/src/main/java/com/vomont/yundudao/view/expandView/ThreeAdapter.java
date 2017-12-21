package com.vomont.yundudao.view.expandView;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ThreeAdapter extends BaseAdapter {

	private List<DeviceInfo> mlist;

	private Context context;

	private int selectPosition;

	public ThreeAdapter(List<DeviceInfo> mlist, Context context) {
		this.mlist = mlist;
		this.context = context;
	}

	@Override
	public int getCount() {
		return null != mlist ? mlist.size() : 0;
	}

	public void setPosition(int selectPosition) {
		this.selectPosition = selectPosition;
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
					R.layout.item_adapter, null);
			holder.text = (TextView) convertView.findViewById(R.id.item_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.text.setText(mlist.get(position).getDevicename());
		if (selectPosition == position) {
			holder.text.setPressed(true);
			holder.text.setSelected(true);
		} else {
			holder.text.setPressed(false);
			holder.text.setSelected(false);
		}

		return convertView;
	}

	class Holder {
		TextView text;
	}

}
