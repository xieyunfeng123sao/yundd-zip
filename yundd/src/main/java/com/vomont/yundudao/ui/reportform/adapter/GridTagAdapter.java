package com.vomont.yundudao.ui.reportform.adapter;

import java.util.ArrayList;
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

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TagInfo;

@SuppressLint("InflateParams")
public class GridTagAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<TagInfo> mlist;
    
    private List<TagInfo> need_tags;
    
    OnCheckClickListener onClickListener;
    
    private boolean isCheck = false;
    
    public GridTagAdapter(Context context)
    {
        this.context = context;
        need_tags = new ArrayList<TagInfo>();
    }
    
    public void setClearCheck(boolean isCheck)
    {
        this.isCheck = isCheck;
        if (!isCheck)
        {
            need_tags.clear();
        }
        notifyDataSetChanged();
    }
    
    public void setData(List<TagInfo> mlist)
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_form_type, null);
            holder = new Holder();
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.item_select_form_type);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        holder.checkBox.setChecked(isCheck);
        
        holder.checkBox.setText(mlist.get(position).getTypename());
        holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    if (!need_tags.contains(mlist.get(position)))
                        need_tags.add(mlist.get(position));
                }
                else
                {
                    need_tags.remove(mlist.get(position));
                }
                if (onClickListener != null)
                {
                    onClickListener.onChange(need_tags, isChecked);
                }
            }
        });
        return convertView;
    }
    
    class Holder
    {
        CheckBox checkBox;
    }
    
    public void setCheckBoxOnClickListener(OnCheckClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }
    
    public interface OnCheckClickListener
    {
        void onChange(List<TagInfo> mlist, boolean isChecked);
        
    }
    
}
