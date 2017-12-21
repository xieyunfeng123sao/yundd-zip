package com.vomont.yundudao.ui.patrol.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalInfo;

public class CanLookAdapter extends BaseAdapter {

	private Context context;

	private List<DetalInfo> mlist;

	private OnCheckClickListener onCheckClickListener;

	public CanLookAdapter(Context context, List<DetalInfo> mlist) {
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CanLookHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_canlook, null);
			holder = new CanLookHolder();
			holder.item_canlook_name = (TextView) convertView
					.findViewById(R.id.item_canlook_name);
			holder.item_canlook_check = (CheckBox) convertView
					.findViewById(R.id.item_canlook_check);
			convertView.setTag(holder);
		} else {
			holder = (CanLookHolder) convertView.getTag();
		}
		holder.item_canlook_name.setText(mlist.get(position).getName());

		holder.item_canlook_check
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							onCheckClickListener.hasChcked(position);
						} else {
							onCheckClickListener.noChecked(position);
						}
					}
				});

		return convertView;
	}

	class CanLookHolder {
		TextView item_canlook_name;
		CheckBox item_canlook_check;

	}

	public void setCheckListener(OnCheckClickListener onCheckClickListener) {
		this.onCheckClickListener = onCheckClickListener;
	}

	public interface OnCheckClickListener {
		void hasChcked(int position);

		void noChecked(int position);
	}
}
