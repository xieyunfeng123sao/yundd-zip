package com.vomont.yundudao.ui.createproblem.adapter;

import java.io.File;
import java.util.List;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.addpic.LocalMedia;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

@SuppressLint("InflateParams") public class SendImgGridAdapter extends BaseAdapter
{
    private Context context;
    
    private List<LocalMedia> mlist;
    
    public SendImgGridAdapter(Context context, List<LocalMedia> mlist)
    {
        this.context = context;
        this.mlist = mlist;
    }
    
    @Override
    public int getCount()
    {
        return null != mlist ? (mlist.size() < 9 ? (mlist.size() + 1) : mlist.size()) : 0;
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
        SendGridHolder holder = null;
        if (convertView == null)
        {
            holder = new SendGridHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_problem_img, null);
            holder.img = (ImageView)convertView.findViewById(R.id.item_problem_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (SendGridHolder)convertView.getTag();
        }
       
        if (position == (mlist.size()))
        {
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.addimg));
        }
        else
        {
            Glide.with(context.getApplicationContext()).load(new File(mlist.get(position).getPath())).thumbnail(0.1f).into( holder.img);
//            LoadLocalImageUtil.getInstance().displayFromSDCard(mlist.get(position).getPath(), holder.img);
        }
        
        notifyDataSetChanged();
        return convertView;
    }
    
    class SendGridHolder
    {
        ImageView img;
    }
    
}
