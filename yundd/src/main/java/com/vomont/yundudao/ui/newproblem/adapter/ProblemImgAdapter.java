package com.vomont.yundudao.ui.newproblem.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.NewProblemImg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

@SuppressLint("InflateParams")
public class ProblemImgAdapter extends BaseAdapter
{
    Context context;
    
    List<NewProblemImg> mlist;
    
    public ProblemImgAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setData(List<NewProblemImg> mlist)
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
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_problem_img_list, null);
            holder = new Holder();
            holder.item_problem_img = (ImageView)convertView.findViewById(R.id.item_problem_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }

        Glide.with(context.getApplicationContext()).load(mlist.get(position).getImageurl()).placeholder(R.drawable.qcenter_factorypic).error(R.drawable.qcenter_factorypic).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.item_problem_img);
        return convertView;
    }
    
    class Holder
    {
        ImageView item_problem_img;
    }
    
}
