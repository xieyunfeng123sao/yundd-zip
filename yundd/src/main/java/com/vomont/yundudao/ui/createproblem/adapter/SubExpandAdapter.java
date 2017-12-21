package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class SubExpandAdapter extends BaseExpandableListAdapter
{
    private List<FactoryInfo> mlist;
    
    private Context context;
    
    int parentP = -1;
    
    int childP = -1;
    
    public SubExpandAdapter(Context context, List<FactoryInfo> mlist)
    {
        this.context = context;
        this.mlist = mlist;
    }
  
    @Override
    public int getGroupCount()
    {
        return null != mlist ? mlist.size() : 0;
    }
    
    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mlist.get(groupPosition).getMlist().size();
    }
    
    @Override
    public Object getGroup(int groupPosition)
    {
        return mlist.get(groupPosition);
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        
        return mlist.get(groupPosition).getMlist().get(childPosition);
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
        holder.name.setText(mlist.get(groupPosition).getFactoryname());
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
        holder.devName.setText(mlist.get(groupPosition).getMlist().get(childPosition).getSubfactoryname());
        // 重点代码
        holder.devName.setTextColor(context.getResources().getColor(R.color.text_color));
        if (parentP != -1 && childP != -1)
        {
            if (parentP == groupPosition && this.childP == childPosition)
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
    
    public void setNotify(int position, int childPosition)
    {
        parentP = position;
        childP = childPosition;
        notifyDataSetChanged();
    }
    
}
