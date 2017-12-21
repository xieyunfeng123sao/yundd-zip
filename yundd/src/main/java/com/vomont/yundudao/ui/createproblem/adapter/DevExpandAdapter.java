package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.ArrayList;
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
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.SubFactory;

@SuppressLint("InflateParams")
public class DevExpandAdapter extends BaseExpandableListAdapter
{
    
    private List<SubFactory> mlist;
    
    private Context context;
    
    // 用来装载某个item是否被选中
    SparseBooleanArray selected;
    
    int parentPosition = -1;
    
    int childPosition = -1;
    
    private List<DeviceInfo> devlist = new ArrayList<DeviceInfo>();
    
    private List<SelectedPosition> positionMlist = new ArrayList<DevExpandAdapter.SelectedPosition>();
    
    public DevExpandAdapter(Context context, List<SubFactory> mlist)
    {
        this.context = context;
        this.mlist = mlist;
        selected = new SparseBooleanArray();
    }
    
    @Override
    public int getGroupCount()
    {
        return null != mlist ? mlist.size() : 0;
    }
    
    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mlist.get(groupPosition).getMlist_device().size();
    }
    
    @Override
    public Object getGroup(int groupPosition)
    {
        return mlist.get(groupPosition);
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mlist.get(groupPosition).getMlist_device().get(childPosition);
    }
    
    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }
    
    @Override
    public boolean hasStableIds()
    {
        return true;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        ExpandSubHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_subfactory, null);
            holder = new ExpandSubHolder();
            holder.name = (TextView)convertView.findViewById(R.id.expand_sub_name);
            holder.img = (ImageView)convertView.findViewById(R.id.expand_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ExpandSubHolder)convertView.getTag();
        }
        holder.name.setText(mlist.get(groupPosition).getSubfactoryname());
        return convertView;
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        ExpandDevHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_dev, null);
            holder = new ExpandDevHolder();
            holder.devName = (TextView)convertView.findViewById(R.id.expand_dev_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ExpandDevHolder)convertView.getTag();
        }
        holder.devName.setText(mlist.get(groupPosition).getMlist_device().get(childPosition).getDevicename());
        // 重点代码
        holder.devName.setTextColor(context.getResources().getColor(R.color.text_color));
        for (int i = 0; i < positionMlist.size(); i++)
        {
            if (positionMlist.get(i).groupPosition == groupPosition && positionMlist.get(i).selectedPostion == childPosition)
            {
                holder.devName.setTextColor(context.getResources().getColor(R.color.main_color));
            }
        }
        if (positionMlist.size() == 0)
        {
            holder.devName.setTextColor(context.getResources().getColor(R.color.text_color));
        }
        if(parentPosition!=-1&&childPosition!=-1)
        {
            if(parentPosition==groupPosition&&this.childPosition==childPosition)
            {
                holder.devName.setTextColor(context.getResources().getColor(R.color.main_color));
            }
        }
        return convertView;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
    
    class ExpandSubHolder
    {
        TextView name;
        
        ImageView img;
    }
    
    class ExpandDevHolder
    {
        TextView devName;
    }
    
    class SelectedPosition
    {
        int groupPosition;
        
        int selectedPostion;
    }
    
    public List<DeviceInfo> getSelectedDev()
    {
        return devlist;
    }
    
    public void setSelectedItem(int groupPosition, int selected)
    {
        SelectedPosition selectedPosition = new SelectedPosition();
        selectedPosition.groupPosition = groupPosition;
        selectedPosition.selectedPostion = selected;
        if (positionMlist.size() != 0)
        {
            int removePosition = -1;
            for (int i = 0; i < positionMlist.size(); i++)
            {
                if (positionMlist.get(i).groupPosition == groupPosition && positionMlist.get(i).selectedPostion == selected)
                {
                    removePosition = i;
                }
            }
            if (removePosition != -1)
            {
                positionMlist.remove(removePosition);
                devlist.remove(removePosition);
            }
            else
            {
                devlist.add(mlist.get(groupPosition).getMlist_device().get(selected));
                positionMlist.add(selectedPosition);
            }
            
        }
        else
        {
            devlist.add(mlist.get(groupPosition).getMlist_device().get(selected));
            positionMlist.add(selectedPosition);
        }
    }
    
    public void  setNotify(int position,int childPosition)
    {
        parentPosition=position;
        this.childPosition=childPosition;
        notifyDataSetChanged();
    }
    
}
