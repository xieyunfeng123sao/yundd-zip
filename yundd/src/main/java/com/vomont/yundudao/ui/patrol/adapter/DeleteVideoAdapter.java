package com.vomont.yundudao.ui.patrol.adapter;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TypeVideo;
import com.vomont.yundudao.utils.BitmapUtil;

@SuppressLint({"InflateParams", "HandlerLeak"})
public class DeleteVideoAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<TypeVideo> mlist;
    
    private static final int TYPE_CATEGORY_ITEM = 0;
    
    private static final int TYPE_ITEM = 1;
    
    private OnCheckBoxChangeListenr onCheckBoxChangeListenr;
    
    public DeleteVideoAdapter(Context context, List<TypeVideo> mlist)
    {
        this.mlist = mlist;
        this.context = context;
    }
    
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
        DeNoLoadHolder holder = null;
        DeHeaderHolder headerHolder = null;
        int itemViewType = getItemViewType(position);
        switch (itemViewType)
        {
            case TYPE_CATEGORY_ITEM:
                if (convertView == null)
                {
                    headerHolder = new DeHeaderHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_list_header, null);
                    headerHolder.textView = (TextView)convertView.findViewById(R.id.list_header);
                    convertView.setTag(headerHolder);
                }
                else
                {
                    headerHolder = (DeHeaderHolder)convertView.getTag();
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
                    holder = new DeNoLoadHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_video_delete, null);
                    holder.ll_select_video = (LinearLayout)convertView.findViewById(R.id.ll_select_video);
                    holder.img = (ImageView)convertView.findViewById(R.id.video_img);
                    holder.desp = (TextView)convertView.findViewById(R.id.video_desp);
                    holder.name = (TextView)convertView.findViewById(R.id.video_sub);
                    holder.length = (TextView)convertView.findViewById(R.id.video_length);
                    holder.delete_video_checkbox = (CheckBox)convertView.findViewById(R.id.delete_video_checkbox);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (DeNoLoadHolder)convertView.getTag();
                }
                if (mlist.get(0).getMlist() != null || mlist.get(1).getMlist() != null)
                {
                    final int onePostion;
                    final int twoPostion;
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
                    asyncloadImage(holder.img,
                        mlist.get(onePostion).getMlist().get(twoPostion).getVideoPath() + "/" + mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".mp4",
                        onePostion,
                        twoPostion);
                    holder.name.setText(mlist.get(onePostion).getMlist().get(twoPostion).getSubname());
                    holder.desp.setText(mlist.get(onePostion).getMlist().get(twoPostion).getDesp());
                    MediaPlayer player = new MediaPlayer();
                    try
                    {
                        player.setDataSource(mlist.get(onePostion).getMlist().get(twoPostion).getVideoPath() + "/" + mlist.get(onePostion).getMlist().get(twoPostion).getName() + ".mp4");
                        player.prepare();
                        if (mlist.get(onePostion).getType().equals("未上传"))
                        {
                            holder.length.setTextColor(context.getResources().getColor(R.color.error_color));
                        }
                        else
                        {
                            holder.length.setTextColor(context.getResources().getColor(R.color.text_color));
                        }
                        holder.length.setText(mlist.get(onePostion).getType());
                        final DeNoLoadHolder holder2 = holder;
                        
                        holder.ll_select_video.setOnClickListener(new OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (holder2.delete_video_checkbox.isChecked())
                                {
                                    holder2.delete_video_checkbox.setChecked(false);
                                }
                                else
                                {
                                    holder2.delete_video_checkbox.setChecked(true);
                                }
                            }
                        });
                        holder.delete_video_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
                        {
                            
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                            {
                                if (onCheckBoxChangeListenr != null)
                                {
                                    if (isChecked)
                                    {
                                        onCheckBoxChangeListenr.add(onePostion, twoPostion);
                                    }
                                    else
                                    {
                                        onCheckBoxChangeListenr.delete(onePostion, twoPostion);
                                    }
                                }
                                
                            }
                        });
                        
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
                
                break;
            
            default:
                break;
        }
        
        return convertView;
    }
    
    private void asyncloadImage(final ImageView imageView, final String path, final int onePostion, final int twoPostion)
    {
        final Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                // if (msg.what == 1)
                // {
                Bitmap bitmap = (Bitmap)msg.obj;
                if (imageView != null && bitmap != null)
                {
                    imageView.setImageBitmap(bitmap);
                }
                // }
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
                        Bitmap bitmap = BitmapUtil.getVideoThumbnail(path);
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
    
    class DeHeaderHolder
    {
        TextView textView;
    }
    
    class DeNoLoadHolder
    {
        ImageView img;
        
        TextView desp;
        
        TextView name;
        
        TextView length;
        
        CheckBox delete_video_checkbox;
        
        LinearLayout ll_select_video;
    }
    
    public void setOnCheckBoxListener(OnCheckBoxChangeListenr onCheckBoxChangeListenr)
    {
        this.onCheckBoxChangeListenr = onCheckBoxChangeListenr;
    }
    
    public interface OnCheckBoxChangeListenr
    {
        void add(int postion, int childPostion);
        
        void delete(int postion, int childPostion);
    }
}
