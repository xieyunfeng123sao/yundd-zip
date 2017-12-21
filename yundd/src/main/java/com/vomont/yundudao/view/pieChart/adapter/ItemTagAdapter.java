package com.vomont.yundudao.view.pieChart.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TagInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemTagAdapter extends BaseAdapter
{
    
    private List<TagInfo> mlist;
    
    private Context context;
    
    public ItemTagAdapter(Context context, List<TagInfo> mlist)
    {
        this.context = context;
        this.mlist = mlist;
    }
    
    @Override
    public int getCount()
    {
        return null != mlist ? mlist.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return mlist.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.brochart_item, null);
            holder = new Holder();
            holder.view = convertView.findViewById(R.id.item_color);
            holder.name = (TextView)convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        holder.view.setBackgroundColor(mlist.get(position).getColor());
        holder.name.setText(mlist.get(position).getTypename());
        
        return convertView;
    }
    
    class Holder
    {
        View view;
        
        TextView name;
    }
    
}
