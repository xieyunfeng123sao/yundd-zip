package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ProblemTypeInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ProblemTypeAdapter  extends BaseAdapter{


	private Context context;
	
	private List<ProblemTypeInfo> mlist;
	
	public ProblemTypeAdapter( Context context,List<ProblemTypeInfo> mlist) {
		this.context=context;
		this.mlist=mlist;
	}
	@Override
	public int getCount() {
		return null!=mlist?mlist.size():0;
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
		ProblemTyperHolder holder=null;
		if(convertView==null)
		{
			holder=new ProblemTyperHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_adapter, null);
			holder.name=(TextView) convertView.findViewById(R.id.item_name);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ProblemTyperHolder) convertView.getTag();
		}
		holder.name.setText(mlist.get(position).getTypename());
		return convertView;
	}
	
	class ProblemTyperHolder
	{
		TextView name;
	}

}
