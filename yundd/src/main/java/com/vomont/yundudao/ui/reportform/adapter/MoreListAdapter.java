package com.vomont.yundudao.ui.reportform.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.googlecode.mp4parser.boxes.MLPSpecificBox;
import com.vomont.fileloadsdk.ACache;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.FormSubInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.bean.TagInfo;
import com.vomont.yundudao.view.pieChart.BrokenLineChartView;
import com.vomont.yundudao.view.pieChart.LineChartView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class MoreListAdapter extends BaseAdapter
{
    
    private Context context;
    
    private Map<Integer, List<FormSubInfo>> map;
    
    private List<Integer> list_key;
    
    long startTime;
    
    long endTime;
    
    List<TagInfo> tagInfos;
    
    public MoreListAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setData(Map<Integer, List<FormSubInfo>> map, long startTime, long endTime, List<TagInfo> tagInfos)
    {
        this.map = map;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tagInfos = tagInfos;
        list_key = new ArrayList<Integer>();
        // 定义一个用来存放key列表
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext())
        {
            list_key.add(iter.next());
        }
        Collections.sort(list_key);
    }
    
    @Override
    public int getCount()
    {
        return null != list_key ? list_key.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return map.get(list_key.get(position));
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_more_report, null);
            holder = new Holder();
            holder.item_more_report_name = (TextView)convertView.findViewById(R.id.item_more_report_name);
            holder.item_linechartview = (BrokenLineChartView)convertView.findViewById(R.id.item_linechartview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        ACache aCache = ACache.get(context);
        FactoryBean factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        holder.item_more_report_name.setText("");
        holder.item_linechartview.setData(map.get(list_key.get(position)), startTime, endTime, tagInfos);
        if (factoryBean != null && factoryBean.getSubfactorys() != null)
        {
            String name = "";
            for (SubFactory subFactory : factoryBean.getSubfactorys())
            {
                if (subFactory.getSubfactoryid() == list_key.get(position))
                {
                    // 过滤巡查点添加名字
                    name = subFactory.getSubfactoryname();
                    for (FactoryInfo factoryInfo : factoryBean.getFactorys())
                    {
                        if (factoryInfo.getFactoryid() == subFactory.getOwnerfactoryid())
                        {
                            // 过滤巡查点添加名字
                            name = factoryInfo.getFactoryname() + " / " + name;
                        }
                    }
                    
                }
            }
            holder.item_more_report_name.setText(name);
        }
        return convertView;
    }
    
    class Holder
    {
        TextView item_more_report_name;
        
        BrokenLineChartView item_linechartview;
    }
}
