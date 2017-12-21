package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class PopuFactoryAdapter extends BaseAdapter {

	private Context context;
	
	private List<FactoryInfo> mlist;
	
	public PopuFactoryAdapter(Context context,List<FactoryInfo> mlist) {
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
		PopuFacHolder holder=null;
		if(convertView==null)
		{
			holder=new PopuFacHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_popu_factory, null);
			holder.name=(TextView) convertView.findViewById(R.id.popu_factory_name);
			convertView.setTag(holder);
		}
		else
		{
			holder=(PopuFacHolder) convertView.getTag();
		}
		holder.name.setBackgroundColor(context.getResources().getColor(R.color.white));
		holder.name.setText(mlist.get(position).getFactoryname());
		
		return convertView;
	}
	
	class PopuFacHolder
	{
		TextView name;
	}
}
