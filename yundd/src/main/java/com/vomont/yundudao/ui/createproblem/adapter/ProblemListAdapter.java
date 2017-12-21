package com.vomont.yundudao.ui.createproblem.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.ProblemListlInfo;
import com.vomont.yundudao.bean.ProblemTypeInfo;
import com.vomont.yundudao.utils.ACache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint({"SimpleDateFormat", "InflateParams"})
public class ProblemListAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<ProblemListlInfo> mlist;
    
    private List<ProblemTypeInfo> info_mlist;
    
    private List<FactoryInfo> factory_mlist;
    
    public ProblemListAdapter(Context context, List<ProblemListlInfo> mlist, List<ProblemTypeInfo> info_mlist, List<FactoryInfo> factory_mlist)
    {
        this.context = context;
        this.mlist = mlist;
        this.info_mlist = info_mlist;
        this.factory_mlist = factory_mlist;
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
        ProListHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_problem_list, null);
            holder = new ProListHolder();
            holder.problem_name = (TextView)convertView.findViewById(R.id.problem_name);
            holder.problem_tag = (TextView)convertView.findViewById(R.id.problem_tag);
            holder.problem_factory = (TextView)convertView.findViewById(R.id.problem_factory);
            holder.problem_time = (TextView)convertView.findViewById(R.id.problem_time);
            holder.problem_state = (TextView)convertView.findViewById(R.id.problem_state);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ProListHolder)convertView.getTag();
        }
        
        for (int i = 0; i < info_mlist.size(); i++)
        {
            if (info_mlist.get(i).getTypeid() == mlist.get(position).getProblemtypeid())
            {
                holder.problem_tag.setText(info_mlist.get(i).getTypename());
            }
        }
        
        ACache aCache=ACache.get(context);
        FactoryBean factoryBean=      (FactoryBean)aCache.getAsObject("factoryBean");
        if(factoryBean!=null&&factoryBean.getDevices()!=null)
        {
            for( DeviceInfo deviceInfo:factoryBean.getDevices())
            {
                if(deviceInfo.getDeviceid()==mlist.get(position).getRelateddeviceid())
                {
                    holder.problem_name.setText(deviceInfo.getDevicename());
                }
            }
        }
        
        holder.problem_tag.setText(mlist.get(position).getProblemtypename());
        holder.problem_factory.setText("现场巡视");
        if (factory_mlist != null && factory_mlist.size() != 0)
            for (int i = 0; i < factory_mlist.size(); i++)
            {
                for (int j = 0; j < factory_mlist.get(i).getMlist().size(); j++)
                {
                    for (int m = 0; m < factory_mlist.get(i).getMlist().get(j).getMlist_device().size(); m++)
                    {
                        if (mlist.get(position).getRelateddeviceid() == factory_mlist.get(i).getMlist().get(j).getMlist_device().get(m).getDeviceid())
                        {
                            holder.problem_factory.setText(factory_mlist.get(i).getFactoryname());
                        }
                    }
                }
            }
        SimpleDateFormat spFormat = new SimpleDateFormat("yyyy.MM.dd");
        if(mlist!=null&&mlist.size()!=0)
        {
            String date = spFormat.format(new Date(mlist.get(position).getCreattime() * 1000));
            holder.problem_time.setText(date);
        }
        switch (mlist.get(position).getProblemstatus())
        {
            case 0:
                holder.problem_state.setText("");
                break;
            case 1:
                holder.problem_state.setText("不合格");
                holder.problem_state.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
                break;
            case 2:
                holder.problem_state.setText("已整改");
                holder.problem_state.setTextColor(context.getResources().getColor(R.color.zhenggai));
                break;
            case 3:
                holder.problem_state.setText("无需整改");
                holder.problem_state.setTextColor(context.getResources().getColor(R.color.zhenggai));
                break;
            case 4:
                holder.problem_state.setText("整改通过");
                holder.problem_state.setTextColor(context.getResources().getColor(R.color.pinglun));
                break;
            case 5:
                holder.problem_state.setText("整改未通过");
                holder.problem_state.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
                break;
            default:
                break;
        }
        return convertView;
    }
    
    class ProListHolder
    {
        TextView problem_name;
        
        TextView problem_tag;
        
        TextView problem_factory;
        
        TextView problem_time;
        
        TextView problem_state;
    }
    
}
