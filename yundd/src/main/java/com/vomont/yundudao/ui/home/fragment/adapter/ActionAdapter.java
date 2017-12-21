package com.vomont.yundudao.ui.home.fragment.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ActionInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ActionAdapter extends BaseAdapter
{
    private Context context;
    
    private List<ActionInfo> mlist;
    
    public ActionAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setData(List<ActionInfo> mlist)
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
    
    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_action_list, null);
            holder = new Holder();
            holder.action_name = (TextView)convertView.findViewById(R.id.action_name);
            holder.action_desp = (TextView)convertView.findViewById(R.id.action_desp);
            holder.action_time = (TextView)convertView.findViewById(R.id.action_time);
            holder.action_desp_txt = (TextView)convertView.findViewById(R.id.action_desp_txt);
            holder.line_view=convertView.findViewById(R.id.line_view);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        holder.action_name.setText(mlist.get(position).getActionaccountname() + " ");
        switch (mlist.get(position).getActionresult())
        {
            case 0:
                holder.action_desp.setText("评论");
                holder.action_desp.setTextColor(context.getResources().getColor(R.color.pinglun));
                break;
            case 1:
                holder.action_desp.setText("创建问题");
                holder.action_desp.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
                break;
            case 2:
                holder.action_desp.setText("已整改");
                holder.action_desp.setTextColor(context.getResources().getColor(R.color.zhenggai));
                break;
            case 3:
                holder.action_desp.setText("无需整改");
                holder.action_desp.setTextColor(context.getResources().getColor(R.color.zhenggai));
                break;
            case 4:
                holder.action_desp.setText("整改通过");
                holder.action_desp.setTextColor(context.getResources().getColor(R.color.pinglun));
                break;
            case 5:
                holder.action_desp.setText("整改未通过");
                holder.action_desp.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
                break;
            default:
                break;
        }
        
        String desp = mlist.get(position).getActiondesp();
        if (desp != null&&!desp.equals(""))
        {
            try
            {
                holder.action_desp_txt.setVisibility(View.VISIBLE);
                holder.action_desp_txt.setText(URLDecoder.decode(desp, "utf-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                holder.action_desp_txt.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.action_desp_txt.setVisibility(View.GONE);
        }
        //yyyy年MM月dd日 
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日  HH:mm");
        String time = format.format(new Date(mlist.get(position).getActiontime() * 1000));
        if (time != null)
            holder.action_time.setText(time);
        return convertView;
    }
    
    class Holder
    {
        TextView action_name;
        
        TextView action_desp;
        
        TextView action_time;
        
        TextView action_desp_txt;
        
        View line_view;
    }
    
}
