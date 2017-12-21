package com.vomont.yundudao.ui.createproblem.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalInfo;

@SuppressLint("InflateParams") public class DetalAapter extends BaseAdapter implements SectionIndexer
{
    
    private Context context;
    
    private List<DetalInfo> mlist;
    
    private int type;
    
    
    private int selectedPosition = -1;
    
    public DetalAapter(Context context, List<DetalInfo> mlist, int type)
    {
        this.context = context;
        this.mlist = mlist;
        this.type = type;
    }
    
    public void updateListView(List<DetalInfo> list)
    {
        this.mlist = list;
        notifyDataSetChanged();
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
        
        DetalHolder holder = null;
        final DetalInfo mContent = mlist.get(position);
        if (convertView == null)
        {
            holder = new DetalHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detal_listview, null);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.detal_check);
            holder.item_sure_bg = (LinearLayout)convertView.findViewById(R.id.item_sure_bg);
            holder.text = (TextView)convertView.findViewById(R.id.detal_name);
            holder.tvLetter = (TextView)convertView.findViewById(R.id.detal_catalog);
            convertView.setTag(holder);
        }
        else
        {
            holder = (DetalHolder)convertView.getTag();
        }
        
        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section))
        {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(mContent.getSortLetters());
        }
        else
        {
            holder.tvLetter.setVisibility(View.GONE);
        }
        if (type == 0)
        {
            holder.checkBox.setVisibility(View.GONE);
        }
        else
        {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        holder.text.setText(mlist.get(position).getName());
        if (selectedPosition == position)
        {
            holder.text.setTextColor(context.getResources().getColor(R.color.white));
            holder.item_sure_bg.setBackgroundColor(context.getResources().getColor(R.color.alertdialog_line));
        }
        else
        {
            holder.text.setTextColor(context.getResources().getColor(R.color.black));
            holder.item_sure_bg.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        
        return convertView;
    }
    
    class DetalHolder
    {
        CheckBox checkBox;
        
        LinearLayout item_sure_bg;
        
        TextView text;
        
        TextView tvLetter;
    }
    
    @Override
    public Object[] getSections()
    {
        return null;
    }
    
  
    
    @SuppressLint("DefaultLocale")
    @Override
    public int getPositionForSection(int sectionIndex)
    {
        for (int i = 0; i < getCount(); i++)
        {
            String sortStr = mlist.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex)
            {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int getSectionForPosition(int position)
    {
        return mlist.get(position).getSortLetters().charAt(0);
    }
    
    public void setSelectedPosition(int position)
    {
        this.selectedPosition = position;
    }
    
}
