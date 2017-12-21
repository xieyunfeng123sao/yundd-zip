package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TagInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class TagItemAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<TagInfo> mlist;
    
    private int selectPosition=-1;
    
    public TagItemAdapter(Context context, List<TagInfo> mlist)
    {
        this.context = context;
        this.mlist = mlist;
    }
    
    public void setPosition(int selectPosition)
    {
        this.selectPosition = selectPosition;
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
        
        TagItemHolder holder = null;
        if (convertView == null)
        {
            holder = new TagItemHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_nexttagitem, null);
            holder.text = (TextView)convertView.findViewById(R.id.tagitem_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (TagItemHolder)convertView.getTag();
        }
        holder.text.setText(mlist.get(position).getTypename());
        
        if (selectPosition == position)
        {
            holder.text.setPressed(true);
            holder.text.setSelected(true);
        }
        else
        {
            holder.text.setPressed(false);
            holder.text.setSelected(false);
        }
        return convertView;
    }
    
    class TagItemHolder
    {
        TextView text;
    }
    
}
