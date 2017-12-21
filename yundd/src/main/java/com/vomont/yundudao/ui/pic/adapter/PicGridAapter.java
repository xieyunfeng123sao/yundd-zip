package com.vomont.yundudao.ui.pic.adapter;

import java.io.File;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.PicTimeInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class PicGridAapter extends BaseAdapter
{
    
    private Context context;
    
    private List<PicTimeInfo> mlist;
    
    public PicGridAapter(Context context, List<PicTimeInfo> mlist)
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
        
        PicGridHolder picGridHolder = null;
        if (convertView == null)
        {
            picGridHolder = new PicGridHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pic_gridview, null);
            picGridHolder.img = (ImageView)convertView.findViewById(R.id.item_pic_gridview);
            picGridHolder.image_time = (TextView)convertView.findViewById(R.id.image_time);
            convertView.setTag(picGridHolder);
        }
        else
        {
            picGridHolder = (PicGridHolder)convertView.getTag();
        }
        Glide.with(context)
            .load(new File(mlist.get(position).getPath() + File.separator + mlist.get(position).getName()))
            .priority(Priority.LOW)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .thumbnail(0.1f)
            .into(picGridHolder.img);
        String time = mlist.get(position).getTime();
        String daytime = time.substring(8, 10) + ":" + time.substring(10, 12);
        picGridHolder.image_time.setText(daytime);
        return convertView;
    }
    
    class PicGridHolder
    {
        ImageView img;
        
        TextView image_time;
    }
    
}
