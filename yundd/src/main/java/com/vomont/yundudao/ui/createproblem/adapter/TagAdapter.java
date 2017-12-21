package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TagInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TagAdapter extends BaseAdapter {

	private Context context;

	private List<TagInfo> mlist;

	private int selectPosition;

	public TagAdapter(Context context, List<TagInfo> mlist) {
		this.context = context;
		this.mlist = mlist;
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
		TagHolder tagHolder = null;
		if (convertView == null) {
			tagHolder = new TagHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_tag, null);
			tagHolder.text = (TextView) convertView.findViewById(R.id.tag_name);

			convertView.setTag(tagHolder);
		} else {
			tagHolder = (TagHolder) convertView.getTag();
		}

		tagHolder.text.setText(mlist.get(position).getTypename());

		if (selectPosition == position) {
			tagHolder.text.setPressed(true);
			tagHolder.text.setSelected(true);
		} else {
			tagHolder.text.setPressed(false);
			tagHolder.text.setSelected(false);
		}

		return convertView;
	}

	class TagHolder {
		TextView text;
	}

}
