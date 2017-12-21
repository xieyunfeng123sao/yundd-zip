package com.vomont.yundudao.ui.patrol.adapter;

import java.io.File;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.ui.patrol.VideoInfo;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.DensityUtil;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.TimeUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImportAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<VideoInfo> mlist;
    
    public ImportAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setData( List<VideoInfo> mlist)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_import, null);
            holder = new Holder();
            holder.img = (ImageView)convertView.findViewById(R.id.item_local_img);
            holder.length = (TextView)convertView.findViewById(R.id.item_local_length);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        Glide.with(Appcation.context).load(new File(mlist.get(position).getPath())).thumbnail(0.1f).skipMemoryCache(true).override(DensityUtil.dip2px(context,30),DensityUtil.dip2px(context,30)).into(holder.img);
        holder.length.setText(TimeUtil.secToTime((int)(mlist.get(position).getDuration()/1000)));
        return convertView;
    }


  
    
    class Holder
    {
        ImageView img;
        
        TextView length;
    }
    
}
