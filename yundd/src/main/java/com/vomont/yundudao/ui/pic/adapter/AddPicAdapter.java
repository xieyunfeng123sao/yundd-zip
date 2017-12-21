package com.vomont.yundudao.ui.pic.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.addpic.LocalMedia;

@SuppressLint("InflateParams")
public class AddPicAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<LocalMedia> mlist;
    
    private int maxNum;
    
    private OnCheckImageListener onCheckImageListener;
    
    private List<LocalMedia> localMlist;
    
    public AddPicAdapter(Context context)
    {
        this.context = context;
        
    }
    
    public void setList(List<LocalMedia> mlist)
    {
        this.mlist = mlist;
    }
    
    public void setMaxCheck(int maxNum)
    {
        this.maxNum = maxNum;
        localMlist = new ArrayList<LocalMedia>();
        for (int i = 0; i < mlist.size(); i++)
        {
            if (mlist.get(i).getIsChecked())
            {
                localMlist.add(mlist.get(i));
            }
        }
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
        AddPicHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_add_pic, null);
            holder = new AddPicHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.picture);
            holder.ll_check = (LinearLayout)convertView.findViewById(R.id.ll_check);
            holder.textView = (TextView)convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        }
        else
        {
            holder = (AddPicHolder)convertView.getTag();
        }
        Glide.with(context).load(new File(mlist.get(position).getPath())).thumbnail(0.2f).priority(Priority.LOW).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.imageView);
        // LoadLocalImageUtil.getInstance().displayFromSDCard(mlist.get(position).getPath(), holder.imageView);
        setOnclick(holder, position);
        // 重新定义被选中状态 避免被复用导致的假数据的显示问题
        holder.textView.setSelected(mlist.get(position).isChecked());
        return convertView;
    }
    
    private void setOnclick(AddPicHolder holder, final int position)
    {
        final AddPicHolder addPicHolder = holder;
        
        addPicHolder.ll_check.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mlist.get(position).getIsChecked())
                {
                    addPicHolder.textView.setSelected(false);
                    mlist.get(position).setIsChecked(false);
                    if (onCheckImageListener != null)
                    {
                        localMlist.remove(mlist.get(position));
                        onCheckImageListener.getLocalMedias(localMlist);
                    }
                }
                else
                {
                    if (localMlist.size() < maxNum)
                    {
                        addPicHolder.textView.setSelected(true);
                        mlist.get(position).setIsChecked(true);
                        if (onCheckImageListener != null)
                        {
                            localMlist.add(mlist.get(position));
                            onCheckImageListener.getLocalMedias(localMlist);
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "最多不超过" + maxNum + "张图片!", Toast.LENGTH_SHORT).show();
                    }
                    
                }
            }
        });
    }
    
    class AddPicHolder
    {
        ImageView imageView;
        
        TextView textView;
        
        LinearLayout ll_check;
    }
    
    public void setOnCheckImageListener(OnCheckImageListener onCheckImageListener)
    {
        this.onCheckImageListener = onCheckImageListener;
    }
    
    public interface OnCheckImageListener
    {
        void getLocalMedias(List<LocalMedia> mlist);
    }
    
}
