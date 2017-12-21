package com.vomont.yundudao.ui.message.adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.Message;
import com.vomont.yundudao.common.Con_Action;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({"InflateParams", "SimpleDateFormat"})
public class MsgAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Message> mlist;
    
    public MsgAdapter(Context context, List<Message> mlist)
    {
        this.context = context;
        this.mlist = mlist;
    }
    
    @Override
    public int getCount()
    {
        return null != mlist ? mlist.size() : 0;
    }
    
    @Override
    public Object getItem(int arg0)
    {
        return mlist.get(arg0);
    }
    
    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }
    
    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2)
    {
        Holder holder = null;
        if (arg1 == null)
        {
            holder = new Holder();
            arg1 = LayoutInflater.from(context).inflate(R.layout.item_msg_lilst, null);
            holder.item_sys_msg_title = (TextView)arg1.findViewById(R.id.item_sys_msg_title);
            holder.item_sys_msg_date = (TextView)arg1.findViewById(R.id.item_sys_msg_date);
            holder.item_sys_msg_content = (TextView)arg1.findViewById(R.id.item_sys_msg_content);
            holder.item_sys_msg_action = (RelativeLayout)arg1.findViewById(R.id.item_sys_msg_action);
            arg1.setTag(holder);
        }
        else
        {
            holder = (Holder)arg1.getTag();
        }
        
        holder.item_sys_msg_title.setText(mlist.get(arg0).getMsgtitle());
        
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sDateFormat.format(new Date(mlist.get(arg0).getMsgpubtime() * 1000 + 0));
        holder.item_sys_msg_date.setText(sDateFormat.format(new Date(mlist.get(arg0).getMsgpubtime() * 1000 + 0)));
        holder.item_sys_msg_content.setText(mlist.get(arg0).getMsgcontent());
        if (mlist.get(arg0).getMsgcontent().length() >= 200)
        {
            holder.item_sys_msg_action.setVisibility(View.VISIBLE);
            
            holder.item_sys_msg_action.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent();
                    intent.putExtra("info", (Serializable)mlist.get(arg0));
                    intent.setAction(Con_Action.MSG_ALL_ACTION);
                    context.startActivity(intent);
                }
            });
        }
        else
        {
            holder.item_sys_msg_action.setVisibility(View.GONE);
        }
        
        return arg1;
    }
    
    class Holder
    {
        TextView item_sys_msg_title;
        
        TextView item_sys_msg_date;
        
        TextView item_sys_msg_content;
        
        RelativeLayout item_sys_msg_action;
    }
}
