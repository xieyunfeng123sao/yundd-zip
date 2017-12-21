package com.vomont.yundudao.ui.pic.adapter;

import java.io.File;
import java.util.List;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.addpic.LocalMediaFolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams") public class PicListAdapter extends BaseAdapter
{
    
    private List<LocalMediaFolder> mlist;
    
    private Context context;
    
    public PicListAdapter(Context context, List<LocalMediaFolder> mlist)
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
        PicListHolder holder = null;
        if (convertView == null)
        {
            holder = new PicListHolder();
            
            convertView = LayoutInflater.from(context).inflate(R.layout.item_piclist, null);
            holder.img = (ImageView)convertView.findViewById(R.id.first_image);
            holder.name = (TextView)convertView.findViewById(R.id.tv_folder_name);
            holder.num = (TextView)convertView.findViewById(R.id.image_num);
            convertView.setTag(holder);
        }
        else
        {
            holder = (PicListHolder)convertView.getTag();
        }
        if (mlist.get(position).getFirstImagePath() != null)
            Glide.with(context).load(new File(mlist.get(position).getFirstImagePath())).into(holder.img);
//            LoadLocalImageUtil.getInstance().displayFromSDCard(mlist.get(position).getFirstImagePath(), holder.img);
        holder.name.setText(mlist.get(position).getName());
        holder.num.setText("("+mlist.get(position).getImageNum() + ")");
        
        return convertView;
    }
    
    class PicListHolder
    {
        ImageView img;
        
        TextView name;
        
        TextView num;
        
    }
    
}
