package com.vomont.yundudao.view.listbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.common.Con_Action;
import com.vomont.yundudao.ui.factory.adapter.GridViewAdapter;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

@SuppressLint({ "InflateParams", "DefaultLocale" })
public class SortAdapter extends BaseAdapter implements SectionIndexer {

	private List<SubFactory> list = null;

	private Context mContext;

	private int factoryid;
	
	private List<FactoryInfo> mlist;

	public SortAdapter(Context mContext, List<SubFactory> list, int factoryid,List<FactoryInfo> mlist) {
		this.mContext = mContext;
		this.list = list;
		this.factoryid = factoryid;
		this.mlist=mlist;
	}

	public void updateListView(List<SubFactory> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		final SubFactory mContent = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item,
					null);
			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) convertView
					.findViewById(R.id.catalog);
			viewHolder.gridview = (NoScrollGridView) convertView
					.findViewById(R.id.gridview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getSubfactoryname());

		final List<DeviceInfo> mlist_num = new ArrayList<DeviceInfo>();
		if (list.get(position).getMlist_device() != null) {
			for (int i = 0; i < list.get(position).getMlist_device().size(); i++) {
				mlist_num.add(list.get(position).getMlist_device().get(i));
			}
			GridViewAdapter adapter = new GridViewAdapter(mlist_num, mContext);
			viewHolder.gridview.setAdapter(adapter);

			viewHolder.gridview
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int channalposition, long id) {
							if (mlist_num.get(channalposition).getHasright() == 0) {
								return;
							}
							Intent intent = new Intent();
							intent.putExtra("factoryname", list.get(position).getSubfactoryname());
							intent.putExtra("factoryid", factoryid);
							intent.putExtra("subfactoryid", list.get(position)
									.getSubfactoryid());
							intent.putExtra("deviceid", list.get(position)
									.getMlist_device().get(channalposition)
									.getDeviceid());
							intent.putExtra("veyeid", list.get(position)
									.getMlist_device().get(channalposition)
									.getVeyeid());
							intent.putExtra("channalid", list.get(position)
									.getMlist_device().get(channalposition)
									.getVeyechannel());
							intent.putExtra("name", list.get(position)
									.getMlist_device().get(channalposition)
									.getDevicename());
							intent.putExtra("ptz",list.get(position).getMlist_device().get(channalposition).getPtz());
							intent.putExtra("factoryBean", (Serializable) mlist);
							intent.setAction(Con_Action.VEDIO_ACTION);
							mContext.startActivity(intent);
						}
					});
			adapter.notifyDataSetChanged();
		} else {

		}
		return convertView;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */

	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	final static class ViewHolder {
		TextView tvLetter;

		TextView tvTitle;

		NoScrollGridView gridview;
	}
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */

	// @SuppressWarnings("unused")
	// private String getAlpha(String str)
	// {
	// String sortStr = str.trim().substring(0, 1).toUpperCase();
	// if (sortStr.matches("[A-Z]"))
	// {
	// return sortStr;
	// }
	// else
	// {
	// return "#";
	// }
	// }

}
