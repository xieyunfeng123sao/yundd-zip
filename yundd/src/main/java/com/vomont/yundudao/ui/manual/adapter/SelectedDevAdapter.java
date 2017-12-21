package com.vomont.yundudao.ui.manual.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;

public class SelectedDevAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<DeviceInfo> mlist;
    
    private OnDeleteListener onDeleteListener;
    
    private List<FactoryInfo> factoryBean;
    
    public SelectedDevAdapter(Context context, List<DeviceInfo> mlist, List<FactoryInfo> factoryBean)
    {
        this.context = context;
        this.mlist = mlist;
        this.factoryBean = factoryBean;
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
        SelDevHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_manual_dev, null);
            holder = new SelDevHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.item_manual_name);
            holder.imageView = (ImageView)convertView.findViewById(R.id.item_manual_delete);
            convertView.setTag(holder);
        }
        else
        {
            holder = (SelDevHolder)convertView.getTag();
        }
        String factoryName = "";
        String subFactoryName = "";
        String devName = "";
        if (factoryBean != null && factoryBean.size() != 0)
        {
            for (FactoryInfo factoryInfo : factoryBean)
            {
                if (factoryInfo.getMlist() != null && factoryInfo.getMlist().size() != 0)
                {
                    for (SubFactory subFactory : factoryInfo.getMlist())
                    {
                        if (subFactory.getMlist_device() != null && subFactory.getMlist_device().size() != 0)
                        {
                            for (DeviceInfo deviceInfo : subFactory.getMlist_device())
                            {
                                if(deviceInfo.getDeviceid()==mlist.get(position).getDeviceid())
                                {
                                    factoryName = factoryInfo.getFactoryname();
                                    subFactoryName = subFactory.getSubfactoryname();
                                    devName = deviceInfo.getDevicename();
                                }
                              
                            }
                        }
                        
                    }
                    
                }
            }
        }
        
        holder.textView.setText(factoryName + "/" + subFactoryName + "/" + devName);
        
        holder.imageView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
               if(onDeleteListener!=null)
               {
                   onDeleteListener.onClick(position);
               }
            }
        });
        return convertView;
    }
    
    
    
    public void setDeleteClickListener(OnDeleteListener onDeleteListener)
    {
        this.onDeleteListener=onDeleteListener;
    }
    
    
    public interface OnDeleteListener
    {
        void onClick(int position);
    }
    
    class SelDevHolder
    {
        TextView textView;
        
        ImageView imageView;
    }
    
}
