package com.vomont.yundudao.ui.reportform.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FormDevAdapter extends BaseAdapter
{
    private Context context;
    
    private List<FactoryInfo> mlist;
    
    private List<SubFactory> subFactories;
    
    private HashMap<String, SubFactory> map;
    
    private List<String> list_key;
    
    public FormDevAdapter(Context context)
    {
        this.context = context;
    }
    
     public void setData(List<FactoryInfo> mlist, List<SubFactory> subFactories) {
     this.mlist = mlist;
     this.subFactories = subFactories;
     }
    
    // public void setData(HashMap<String, SubFactory> map)
    // {
    // // this.mlist = mlist;
    // // this.subFactories = subFactories;
    // this.map = map;
    // list_key = new ArrayList<String>();
    // Iterator iterator = map.keySet().iterator();
    //
    // while (iterator.hasNext())
    // {
    // list_key.add(iterator.next().toString());
    // }
    // }
    
    @Override
    public int getCount()
    {
        return null != subFactories ? subFactories.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return subFactories.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_form_dev, null);
            holder = new Holder();
            holder.item_form_dev_name = (TextView)convertView.findViewById(R.id.item_form_dev_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        
        String item_name =subFactories.get(position).getSubfactoryname();
        String name = "";
        if(mlist!=null)
        {
            for(FactoryInfo factoryInfo:mlist)
            {
                if(subFactories.get(position).getOwnerfactoryid()==factoryInfo.getFactoryid())
                {
                    name=factoryInfo.getFactoryname();
                    break;
                }
            }
        }
        name = name + " / " + item_name ;
        holder.item_form_dev_name.setText(name);
        
        return convertView;
    }
    
    class Holder
    {
        TextView item_form_dev_name;
    }
}
