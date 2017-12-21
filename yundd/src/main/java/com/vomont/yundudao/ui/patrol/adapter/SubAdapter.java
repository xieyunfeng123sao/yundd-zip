package com.vomont.yundudao.ui.patrol.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;

@SuppressLint("InflateParams")
public class SubAdapter extends BaseExpandableListAdapter {
	private List<FactoryInfo> mlist;

	private Context context;
    //用来装载某个item是否被选中
    SparseBooleanArray selected;
    int old = -1;
    int parentPosition = -1;

	public SubAdapter(Context context, List<FactoryInfo> mlist) {
		this.context = context;
		this.mlist = mlist;
		selected = new SparseBooleanArray();
	}

	@Override
	public int getGroupCount() {
		return null != mlist ? mlist.size() : 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (mlist.get(groupPosition).getMlist() != null) {
			return mlist.get(groupPosition).getMlist().size();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mlist.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (mlist.get(groupPosition).getMlist() != null) {
			return mlist.get(groupPosition).getMlist().get(childPosition);
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ExpandSubHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_expand_subfactory, null);
			holder = new ExpandSubHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.expand_sub_name);
			holder.img = (ImageView) convertView.findViewById(R.id.expand_img);
			convertView.setTag(holder);
		} else {
			holder = (ExpandSubHolder) convertView.getTag();
		}
		holder.name.setText(mlist.get(groupPosition).getFactoryname());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ExpandDevHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_expand_dev, null);
			holder = new ExpandDevHolder();
			holder.devName = (TextView) convertView
					.findViewById(R.id.expand_dev_name);
			convertView.setTag(holder);
		} else {
			holder = (ExpandDevHolder) convertView.getTag();
		}
		holder.devName.setText(mlist.get(groupPosition).getMlist()
				.get(childPosition).getSubfactoryname());
		//重点代码 
        if (selected.get(childPosition)&&this.parentPosition==groupPosition) {
            holder.devName.setTextColor(context.getResources().getColor(
                    R.color.main_color));
        } else {
            holder.devName.setTextColor(context.getResources().getColor(
                    R.color.text_color));
        }
		return convertView;
	}

	class ExpandSubHolder {
		TextView name;

		ImageView img;
	}

	class ExpandDevHolder {
		TextView devName;
	}
	
	public void setSelectedItem(int groupPosition,int selected) {
        this.parentPosition = groupPosition;
        if (old != -1) {
            this.selected.put(old, false);
        }
        this.selected.put(selected, true);
        old = selected;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
