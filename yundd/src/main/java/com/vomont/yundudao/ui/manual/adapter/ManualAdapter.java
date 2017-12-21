package com.vomont.yundudao.ui.manual.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ManualBean;

@SuppressLint("InflateParams")
public class ManualAdapter extends BaseAdapter
{
    private Context context;
    
    private List<ManualBean> mlist;
    OnEditListener onEditListener;
    
    public ManualAdapter(Context context,List<ManualBean> mlist)
    {
        this.context = context;
        this.mlist=mlist;
    }
    
    @Override
    public int getCount()
    {
        return mlist.size();
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
        ManualHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_manual_list, null);
            holder = new ManualHolder();
            holder.item_manual_name=(TextView)convertView.findViewById(R.id.item_manual_name);
            holder.item_manual_time=(TextView)convertView.findViewById(R.id.item_manual_time);
            holder.item_manual_size=(TextView)convertView.findViewById(R.id.item_manual_size);
            holder.item_manual_edit=(ImageView)convertView.findViewById(R.id.item_manual_edit);
            holder.item_manual_back=(TextView)convertView.findViewById(R.id.item_manual_back);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ManualHolder)convertView.getTag();
        }
        holder.item_manual_name.setText(mlist.get(position).getName());
        holder.item_manual_time.setText(mlist.get(position).getLength()+"");
        holder.item_manual_size.setText(mlist.get(position).getDevList().size()+"");
        if(mlist.get(position).getMode()==0)
        {
            holder.item_manual_back.setText("不循环");
        }
        else
        {
            holder.item_manual_back.setText("循环");
        }
        holder.item_manual_edit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onEditListener!=null)
                {
                    onEditListener.onEdit(position);
                }
            }
        });
        return convertView;
    }
    
    class ManualHolder
    {
        TextView item_manual_name;
        
        TextView item_manual_time;
        
        TextView item_manual_size;
        
        TextView item_manual_back;
        
        ImageView item_manual_edit;
        
    }
    public void setOnEditListener(OnEditListener onEditListener)
    {
        this.onEditListener=onEditListener;
    }
    
    public interface OnEditListener
    {
        void onEdit(int position);
    }
    
}
