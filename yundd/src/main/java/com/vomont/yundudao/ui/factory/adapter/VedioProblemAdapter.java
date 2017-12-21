package com.vomont.yundudao.ui.factory.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ProblemListlInfo;
import com.vomont.yundudao.bean.ProblemTypeInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class VedioProblemAdapter extends BaseAdapter {

	private Context context;
	private List<ProblemListlInfo> mlist;
	@SuppressWarnings("unused")
	private List<ProblemTypeInfo> info_mlist;

	public VedioProblemAdapter(Context context, List<ProblemListlInfo> mlist,
			List<ProblemTypeInfo> info_mlist) {
		this.context = context;
		this.mlist = mlist;
		this.info_mlist = info_mlist;
	}

	@Override
	public int getCount() {
		return mlist!=null?(mlist.size()>4?4:mlist.size()):0;
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VedioProblemHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_vedio_problem, null);
			holder = new VedioProblemHolder();
			holder.time = (TextView) convertView
					.findViewById(R.id.item_vedio_problem_time);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_vedio_problem_name);
			holder.type = (TextView) convertView
					.findViewById(R.id.item_vedio_problem_type);
			convertView.setTag(holder);
		} else {
			holder = (VedioProblemHolder) convertView.getTag();
		}
		SimpleDateFormat spFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = spFormat.format(new Date(mlist.get(position)
				.getCreattime() * 1000));
		holder.time.setText(date);
		holder.name.setText(mlist.get(position).getProblemtypename());
		switch (mlist.get(position).getProblemstatus()) {
		case 0:
			holder.type.setText("");
			break;
		case 1:
			holder.type.setText("不合格");
			holder.type.setTextColor(context.getResources().getColor(
					R.color.actionsheet_red));
			break;
		case 2:
			holder.type.setText("已整改");
			holder.type.setTextColor(context.getResources().getColor(
					R.color.zhenggai));
			break;
		case 3:
			holder.type.setText("无需整改");
			holder.type.setTextColor(context.getResources().getColor(
					R.color.zhenggai));
			break;
		case 4:
			holder.type.setText("整改通过");
			holder.type.setTextColor(context.getResources().getColor(
					R.color.pinglun));
			break;
		case 5:
			holder.type.setText("整改未通过");
			holder.type.setTextColor(context.getResources().getColor(
					R.color.actionsheet_red));
			break;
		default:
			break;
		}

		return convertView;
	}

	class VedioProblemHolder {
		TextView time;
		TextView name;
		TextView type;

	}
}
