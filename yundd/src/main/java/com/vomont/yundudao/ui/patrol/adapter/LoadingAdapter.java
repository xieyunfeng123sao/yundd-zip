package com.vomont.yundudao.ui.patrol.adapter;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;

@SuppressLint({"InflateParams", "HandlerLeak"})
public class LoadingAdapter extends BaseAdapter
{
    private List<VideoBean> mlist;
    
    private Context context;
    
    private CheckListener checkListener;
    
    private ACache aCache;
    
    public LoadingAdapter(Context context, List<VideoBean> mlist)
    {
        this.context = context;
        this.mlist = mlist;
        initCache();
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
        LoadingHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_load_proress, null);
            holder = new LoadingHolder();
            holder.ll_delete_view = (LinearLayout)convertView.findViewById(R.id.ll_delete_view);
            holder.load_img = (ImageView)convertView.findViewById(R.id.load_img);
            holder.load_desp = (TextView)convertView.findViewById(R.id.load_desp);
            holder.load_load_state = (TextView)convertView.findViewById(R.id.load_load_state);
            holder.load_pos = (TextView)convertView.findViewById(R.id.load_pos);
            holder.load_progress = (ProgressBar)convertView.findViewById(R.id.load_progress);
            holder.delete_loading_video = (CheckBox)convertView.findViewById(R.id.delete_loading_video);
            convertView.setTag(holder);
        }
        else
        {
            holder = (LoadingHolder)convertView.getTag();
        }
        
        if (mlist.get(position).getDelete() == 0)
        {
            holder.delete_loading_video.setVisibility(View.GONE);
        }
        else
        {
            holder.delete_loading_video.setVisibility(View.VISIBLE);
            final LoadingHolder holder2 = holder;
            holder.ll_delete_view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    if (holder2.delete_loading_video.isChecked())
                    {
                        holder2.delete_loading_video.setChecked(false);
                    }
                    else
                    {
                        holder2.delete_loading_video.setChecked(true);
                    }
                }
            });
            holder.delete_loading_video.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (checkListener != null)
                    {
                        if (isChecked)
                        {
                            checkListener.addPosition(position);
                        }
                        else
                        {
                            checkListener.deletePosition(position);
                        }
                    }
                }
            });
        }
        Glide.with(context).load(new File(VideoManager.video_face_img + "/" + mlist.get(position).getName() + ".jpg")).into(holder.load_img);
        // asyncloadImage(holder.load_img, mlist.get(position).getVideoPath() + "/" + mlist.get(position).getName() +
        // ".mp4", position, mlist.get(position).getName());
        holder.load_desp.setText(mlist.get(position).getDesp());
        if (mlist.get(position).getLoadstate() == 0)
        {
            holder.load_load_state.setText("暂停上传");
        }
        else if (mlist.get(position).getLoadstate() == 1)
        {
            holder.load_load_state.setText("上传中");
        }
        else if(mlist.get(position).getLoadstate() == 2)
        {
            holder.load_load_state.setText("上传完成");
        }
        else
        {
            holder.load_load_state.setText("视频压缩");
        }
        int pos = 0;
        if (mlist.get(position).getPos() < 0)
        {
            pos = 0;
        }
        else
        {
            pos = mlist.get(position).getPos();
        }
        if (pos == 100)
        {
            pos = 99;
        }
        holder.load_pos.setText(pos + "%");
        holder.load_progress.setProgress(mlist.get(position).getPos());
        return convertView;
    }
  
    public void setCheckBoxListener(CheckListener checkListener)
    {
        this.checkListener = checkListener;
    }
    
    private void initCache()
    {
        aCache = ACache.get(context);
    }
    
    /**
     * 给ImageView设置Bitmap
     */
    @SuppressWarnings("unused")
    private void showImageView(ImageView imageView, String path)
    {
        // 对url进行md5编码
        String key = path;
        // 先从cache中找bitmap缓存
        Bitmap bitmap = get(key);
        
        if (bitmap != null)
        {
            // 如果缓存命中
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            // 如果cache miss
            imageView.setBackgroundResource(R.color.alertdialog_line);
        }
    }
    
    /**
     * 将Bitmap put 到 cache中
     */
    @SuppressWarnings("unused")
    private void put(String key, Bitmap bitmap)
    {
        if (get(key) == null)
        {
            aCache.put(key, bitmap);
        }
    }
    
    /**
     * 在Cache中查找bitmap，如果miss则返回null
     */
    private Bitmap get(String key)
    {
        return aCache.getAsBitmap(key);
    }
    
    public interface CheckListener
    {
        void addPosition(int i);
        
        void deletePosition(int i);
    }
    
    class LoadingHolder
    {
        ImageView load_img;
        
        TextView load_desp;
        
        TextView load_load_state;
        
        TextView load_pos;
        
        ProgressBar load_progress;
        
        CheckBox delete_loading_video;
        
        LinearLayout ll_delete_view;
    }
}
