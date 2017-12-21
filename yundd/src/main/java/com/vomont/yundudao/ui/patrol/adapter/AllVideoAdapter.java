package com.vomont.yundudao.ui.patrol.adapter;

import java.io.File;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TypeVideo;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.BitmapUtil;
import android.annotation.SuppressLint;
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

@SuppressLint({"InflateParams", "HandlerLeak"})
public class AllVideoAdapter extends BaseAdapter
{
    private Context context;
    
    private List<TypeVideo> mlist;
    
    private static final int TYPE_CATEGORY_ITEM = 0;
    
    private static final int TYPE_ITEM = 1;
    
//    private int length;
    
//    private ACache aCache;
    
    public AllVideoAdapter(Context context, List<TypeVideo> mlist)
    {
        this.mlist = mlist;
        this.context = context;
//        initCache();
    }
    
//    private void initCache()
//    {
//        aCache = ACache.get(context);
//    }
//    
//    /**
//     * 给ImageView设置Bitmap
//     */
//    private void showImageView(ImageView imageView, String path)
//    {
//        
//        // 对url进行md5编码
//        String key = path;
//        // 先从cache中找bitmap缓存
//        Bitmap bitmap = get(key);
//        
//        if (bitmap != null)
//        {
//            // 如果缓存命中
//            imageView.setImageBitmap(bitmap);
//        }
//        else
//        {
//            // 如果cache miss
//            imageView.setBackgroundResource(R.color.alertdialog_line);
//        }
//    }
    
//    /**
//     * 将Bitmap put 到 cache中
//     */
//    private void put(String key, Bitmap bitmap)
//    {
//        if (get(key) == null)
//        {
//            aCache.put(key, bitmap);
//        }
//    }
    
//    /**
//     * 在Cache中查找bitmap，如果miss则返回null
//     */
//    private Bitmap get(String key)
//    {
//        return aCache.getAsBitmap(key);
//    }
    
    @Override
    public int getCount()
    {
        int count = 0;
        
        if (null != mlist)
        {
            // 所有分类中item的总和是ListVIew Item的总个数
            for (TypeVideo typeVideo : mlist)
            {
                count += typeVideo.getItemCount();
            }
        }
        return count;
    }
    
    @Override
    public Object getItem(int position)
    {
        // 异常情况处理
        if (null == mlist || position < 0 || position > getCount())
        {
            return null;
        }
        
        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = 0;
        
        for (TypeVideo typeVideo : mlist)
        {
            int size = typeVideo.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size)
            {
                return typeVideo.getItem(categoryIndex);
            }
            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }
        
        return null;
    }
    
    @Override
    public int getItemViewType(int position)
    {
        // 异常情况处理
        if (null == mlist || position < 0 || position > getCount())
        {
            return TYPE_ITEM;
        }
        
        int categroyFirstIndex = 0;
        
        for (TypeVideo typeVideo : mlist)
        {
            int size = typeVideo.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == 0)
            {
                return TYPE_CATEGORY_ITEM;
            }
            categroyFirstIndex += size;
        }
        return TYPE_ITEM;
    }
    
    @Override
    public int getViewTypeCount()
    {
        return 2;
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }
    
    @Override
    public boolean isEnabled(int position)
    {
        return getItemViewType(position) != TYPE_CATEGORY_ITEM;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        NoLoadHolder holder = null;
        HeaderHolder headerHolder = null;
        int itemViewType = getItemViewType(position);
        switch (itemViewType)
        {
            case TYPE_CATEGORY_ITEM:
                if (convertView == null)
                {
                    headerHolder = new HeaderHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_list_header, null);
                    headerHolder.textView = (TextView)convertView.findViewById(R.id.list_header);
                    convertView.setTag(headerHolder);
                }
                else
                {
                    headerHolder = (HeaderHolder)convertView.getTag();
                }
                if (position == 0)
                {
                    headerHolder.textView.setText(mlist.get(0).getType());
                }
                else
                {
                    headerHolder.textView.setText(mlist.get(1).getType());
                }
                break;
            case TYPE_ITEM:
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
                if (mlist.get(0).getMlist() != null || mlist.get(1).getMlist() != null)
                {
                    int onePostion, twoPostion;
                    if (position <= mlist.get(0).getMlist().size())
                    {
                        onePostion = 0;
                        twoPostion = position - 1;
                    }
                    else
                    {
                        onePostion = 1;
                        twoPostion = position - (2 + mlist.get(0).getMlist().size());
                    }
                    
//                    if (mlist.get(onePostion).getMlist().get(twoPostion).getSubname()!= null && !mlist.get(onePostion).getMlist().get(twoPostion).getSubname().equals(""))
//                    {
//                        holder.name.setText(mlist.get(onePostion).getMlist().get(twoPostion).getSubname());
//                    }
//                    else
//                    {
//                        holder.name.setText("无");
//                    }
//                    if(mlist.get(onePostion).getMlist().get(twoPostion).getDesp()!=null&&!mlist.get(onePostion).getMlist().get(twoPostion).getDesp().equals(""))
//                    {
//                        holder.desp.setText(mlist.get(onePostion).getMlist().get(twoPostion).getDesp());
//                    }
//                    else
//                    {
//                        holder.desp.setText("无");
//                    }
                    
                    holder.name.setText(mlist.get(onePostion).getMlist().get(twoPostion).getSubname());
                    holder.desp.setText(mlist.get(onePostion).getMlist().get(twoPostion).getDesp());
                    if (mlist.get(onePostion).getType().equals("未上传"))
                    {
                        holder.length.setTextColor(context.getResources().getColor(R.color.error_color));
                    }
                    else
                    {
                        holder.length.setTextColor(context.getResources().getColor(R.color.text_color));
                    }
                    holder.length.setText(mlist.get(onePostion).getType());
                    asyncloadImage(holder.img, holder.length, VideoManager.path + "/" + mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".mp4", onePostion, twoPostion);
                }
                break;
            default:
                break;
        }
        return convertView;
    }
    
    private void asyncloadImage(final ImageView imageView, final TextView textView, final String path, final int onePostion, final int twoPostion)
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
                    imageView.setImageBitmap(bitmap);
                }
                else
                {
                    ImageLoader.getInstance().displayImage("file://"+VideoManager.video_face_img + "/" +mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".jpg", imageView);
//                    Glide.with(context).load(new File(VideoManager.video_face_img + "/" + mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".jpg")).into(imageView);
//                    LoadLocalImageUtil.getInstance().displayFromSDCard(VideoManager.video_face_img + "/" + mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".jpg", imageView);
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
                        File file = new File(VideoManager.video_face_img + "/" + mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".jpg");
                        if (!file.exists())
                        {
                            bitmap = BitmapUtil.getVideoThumbnail(path);
                            BitmapUtil.savePhotoByte(bitmap, VideoManager.video_face_img, mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".jpg");
                        }
                        try
                        {
                            Message msg = new Message();
                            msg.what = 10;
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
    
    class HeaderHolder
    {
        TextView textView;
    }
    
    class NoLoadHolder
    {
        ImageView img;
        
        TextView desp;
        
        TextView name;
        
        TextView length;
    }
}
