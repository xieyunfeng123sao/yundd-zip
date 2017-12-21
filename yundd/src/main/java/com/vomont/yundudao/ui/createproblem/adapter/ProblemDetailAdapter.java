package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ProblemListlInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ProblemDetailAdapter extends BaseAdapter {

	private Context context;

	private List<ProblemListlInfo> mlist;

	public ProblemDetailAdapter(Context context, List<ProblemListlInfo> mlist) {
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
		ProblemDetailHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_problem_list, null);
			holder = new ProblemDetailHolder();
			holder.problem_img = (ImageView) convertView
					.findViewById(R.id.problem_img);
			holder.problem_name = (TextView) convertView
					.findViewById(R.id.problem_name);
			holder.problem_tag = (TextView) convertView
					.findViewById(R.id.problem_tag);
			holder.problem_factory = (TextView) convertView
					.findViewById(R.id.problem_factory);
			holder.problem_other = (TextView) convertView
					.findViewById(R.id.problem_other);
			holder.problem_time = (TextView) convertView
					.findViewById(R.id.problem_time);
			holder.problem_state = (TextView) convertView
					.findViewById(R.id.problem_state);
			convertView.setTag(holder);
		} else {
			holder = (ProblemDetailHolder) convertView.getTag();
		}

		return convertView;
	}

	class ProblemDetailHolder {
		ImageView problem_img;

		TextView problem_name;

		TextView problem_tag;

		TextView problem_factory;

		TextView problem_other;

		TextView problem_time;

		TextView problem_state;
	}

}
