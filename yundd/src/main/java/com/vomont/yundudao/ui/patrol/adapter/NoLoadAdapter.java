package com.vomont.yundudao.ui.patrol.adapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.TimeUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({"InflateParams", "HandlerLeak"})
public class NoLoadAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<VideoBean> mlist;
    
    // private LruCache<String, Bitmap> mLruCache;
    
    private ACache aCache;
    
    public NoLoadAdapter(Context context, List<VideoBean> mlist)
    {
        this.mlist = mlist;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        NoLoadHolder holder = null;
        if (convertView == null)
        {
            holder = new NoLoadHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_load, null);
            holder.img = (ImageView)convertView.findViewById(R.id.video_img);
            holder.desp = (TextView)convertView.findViewById(R.id.video_desp);
            holder.name = (TextView)convertView.findViewById(R.id.video_sub);
            holder.length = (TextView)convertView.findViewById(R.id.video_length);
            convertView.setTag(holder);
        }
        else
        {
            holder = (NoLoadHolder)convertView.getTag();
        }
        asyncloadImage(holder.img, holder.length, mlist.get(position).getVideoPath() + "/" + mlist.get(position).getName() + ".mp4", position, mlist.get(position).getName());
        
        holder.name.setText(mlist.get(position).getSubname());
        
        holder.desp.setText(mlist.get(position).getDesp());
        
        return convertView;
    }
    
    private void asyncloadImage(final ImageView imageView, final TextView textView, final String path, final int position, final String name)
    {
        final Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                Bitmap bitmap = (Bitmap)msg.obj;
                if (imageView != null && bitmap != null)
                {
                    textView.setText(TimeUtil.secToTime(msg.what));
                    imageView.setImageBitmap(bitmap);
                }
                else
                {
                    Glide.with(context).load(new File(VideoManager.video_face_img + "/" + mlist.get(position).getName() + ".jpg")).into(imageView);
                    textView.setText(TimeUtil.secToTime(msg.what));
                }
            }
        };
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // 这个URI是图片下载到本地后的缓存目录中的URI
                    if (path != null)
                    {
                        Bitmap bitmap = null;
                        File file = new File(VideoManager.video_face_img + "/" + mlist.get(position).getName() + ".jpg");
                        if (!file.exists())
                        {
                            bitmap = BitmapUtil.getVideoThumbnail(path);
                            BitmapUtil.savePhotoByte(bitmap, VideoManager.video_face_img, mlist.get(position).getName() + ".jpg");
                        }
                        MediaPlayer player = new MediaPlayer();
                        try
                        {
                            player.setDataSource(path);
                            player.prepare();
                            int length = player.getDuration() / 1000;
                            player.release();
                            Message msg = new Message();
                            msg.what = length;
                            msg.obj = bitmap;
                            mHandler.sendMessage(msg);
                        }
                        catch (IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                        catch (SecurityException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IllegalStateException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
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
    
    class NoLoadHolder
    {
        ImageView img;
        
        TextView desp;
        
        TextView name;
        
        TextView length;
    }
}
