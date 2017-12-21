package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;

import com.vomont.yundudao.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ProblemStatusAdapter extends BaseAdapter {

	private Context context;
	private List<String> mlist;

	public ProblemStatusAdapter(Context context, List<String> mlist) {
		this.context = context;
		this.mlist = mlist;
	}

	@Override
	public int getCount() {
		return null != mlist ? mlist.size() : 0;
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
		ProblemStatusHolder holder = null;
		if (convertView == null) {
			holder = new ProblemStatusHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_problem_status, null);
			holder.name = (TextView) convertView.findViewById(R.id.status_name);
			convertView.setTag(holder);
		} else {
			holder = (ProblemStatusHolder) convertView.getTag();
		}
		holder.name.setText(mlist.get(position));
		return convertView;
	}

	class ProblemStatusHolder {
		TextView name;
	}

}
