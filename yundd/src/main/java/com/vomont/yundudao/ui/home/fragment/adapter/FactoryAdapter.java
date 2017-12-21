package com.vomont.yundudao.ui.home.fragment.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class FactoryAdapter extends BaseAdapter
{
    
    private List<FactoryInfo> mlist;
    
    private Context context;
    
    public FactoryAdapter(List<FactoryInfo> mlist, Context context)
    {
        this.mlist = mlist;
        this.context = context;
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
        if (null == convertView)
        {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fragment_factory, null);
            holder.item_look_about = (TextView)convertView.findViewById(R.id.item_look_about);
            holder.factory_name = (TextView)convertView.findViewById(R.id.factory_name);
            holder.item_num_qu = (TextView)convertView.findViewById(R.id.item_num_qu);
            holder.now_dev_num = (TextView)convertView.findViewById(R.id.now_dev_num);
            holder.dev_num = (TextView)convertView.findViewById(R.id.dev_num);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        int m=0;
        int n=0;
        int onLine=0;
        if(mlist.get(position).getMlist()!=null&&mlist.get(position).getMlist().size()!=0)
        {
            for(int i=0;i<mlist.get(position).getMlist().size();i++)
            {
            	if(mlist.get(position).getMlist().get(i).getMlist_device()!=null&&mlist.get(position).getMlist().get(i).getMlist_device().size()!=0)
            	{
            		m++;
                	for(int j=0;j<mlist.get(position).getMlist().get(i).getMlist_device().size();j++)
                	{
                		if(mlist.get(position).getMlist().get(i).getMlist_device().get(j).getOnline()==1)
                		{
                			onLine++;
                		}
                		else
                		{
                		}
                	}
            	}
            	else
            	{
            	}
            	n=n+mlist.get(position).getMlist().get(i).getMlist_device().size();        	
            }
            holder.item_look_about.setText("可视率"+m*100/mlist.get(position).getMlist().size()+"%"); 
        }
        else
        {
       	 holder.item_look_about.setText("可视率"+0+"%"); 
        }
        holder.factory_name.setText(mlist.get(position).getFactoryname());
        if(mlist.get(position).getMlist()!=null)
        {
        	  holder.item_num_qu.setText(mlist.get(position).getMlist().size()+"");
        }
        holder.dev_num.setText(n+"");
        holder.now_dev_num.setText(onLine+"");
        return convertView;
    }
    class Holder
    {
        TextView item_look_about;
        
        TextView factory_name;
        
        TextView item_num_qu;
        
        TextView now_dev_num;
        
        TextView dev_num;
        
    }
}


