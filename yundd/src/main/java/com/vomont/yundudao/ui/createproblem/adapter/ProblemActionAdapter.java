package com.vomont.yundudao.ui.createproblem.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ActionInfo;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class ProblemActionAdapter extends BaseAdapter {

	private List<ActionInfo> mlist;

	private Context context;

	public ProblemActionAdapter(Context context, List<ActionInfo> mlist) {
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
		ProblemActionHoler holer = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_problem_action, null);
			holer = new ProblemActionHoler();
			holer.name = (TextView) convertView
					.findViewById(R.id.problem_action_name);
			holer.action = (TextView) convertView
					.findViewById(R.id.problem_action);
			holer.content = (TextView) convertView
					.findViewById(R.id.problem_action_sprak);
			holer.time = (TextView) convertView
					.findViewById(R.id.problem_action_date);
			holer.problemcomment=(TextView) convertView.findViewById(R.id.problemcomment);
			convertView.setTag(holer);
		} else {
			holer = (ProblemActionHoler) convertView.getTag();
		}
		holer.name.setText(mlist.get(position).getActionaccountname());
		if(mlist.get(position).getActionresult()==1)
		{
			holer.action.setText("创建问题");
			holer.problemcomment.setVisibility(View.GONE);
		}
		else if(mlist.get(position).getActionresult()==2)
		{
			holer.action.setText("已整改");
			holer.action.setTextColor(context.getResources().getColor(R.color.zhenggai));
		}
		else if(mlist.get(position).getActionresult()==3)
		{
			holer.action.setText("无需整改");
			holer.action.setTextColor(context.getResources().getColor(R.color.zhenggai));
		}
		else if(mlist.get(position).getActionresult()==4)
		{
			holer.action.setText("整改通过");
			holer.action.setTextColor(context.getResources().getColor(R.color.pinglun));
		}
		else if(mlist.get(position).getActionresult()==5)
		{
			holer.action.setText("整改未通过");
			holer.action.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
		}else
		{
			holer.problemcomment.setTextColor(context.getResources().getColor(R.color.pinglun));
		}
		
		try {
			holer.content.setText(URLDecoder.decode(mlist.get(position).getActiondesp(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		holer.time.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(
				mlist.get(position).getActiontime()*1000)));
		return convertView;
	}

	class ProblemActionHoler {
		TextView name;

		TextView action;

		TextView content;

		TextView time;
		TextView problemcomment;

	}
}
