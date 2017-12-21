package com.vomont.yundudao.ui.ip.adapter;

import java.util.List;

import com.vomont.yundudao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemIpAdapter extends BaseAdapter
{
    private Context context;
    
    private List<String> mlist;
    
    public ItemIpAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setData(List<String> mlist)
    {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ip, null);
            holder = new Holder();
            holder.name = (TextView)convertView.findViewById(R.id.item_ip_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        holder.name.setText(mlist.get(position));
        return convertView;
    }
    
    class Holder
    {
        TextView name;
    }
}
