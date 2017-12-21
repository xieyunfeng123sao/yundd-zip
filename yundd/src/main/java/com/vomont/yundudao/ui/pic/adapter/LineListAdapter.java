package com.vomont.yundudao.ui.pic.adapter;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.PicTimeBean;

@SuppressLint("InflateParams")
public class LineListAdapter extends BaseAdapter
{
    
    private List<PicTimeBean> mlist;
    
    private Context context;
    
    private OnCenterPicListener onCenterPicListener;
    
    public LineListAdapter(Context context, List<PicTimeBean> mlist, String from, Intent intent)
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        
        PicTimeHolder picTimeHolder = null;
        if (convertView == null)
        {
            picTimeHolder = new PicTimeHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_timeline, null);
            picTimeHolder.text_date = (TextView)convertView.findViewById(R.id.timeline_date);
            picTimeHolder.timeline_year = (TextView)convertView.findViewById(R.id.timeline_year);
            picTimeHolder.timeline_poit = (ImageView)convertView.findViewById(R.id.timeline_poit);
            picTimeHolder.img1 = (ImageView)convertView.findViewById(R.id.img1);
            picTimeHolder.img2 = (ImageView)convertView.findViewById(R.id.img2);
            picTimeHolder.img3 = (ImageView)convertView.findViewById(R.id.img3);
            
            picTimeHolder.txt1 = (TextView)convertView.findViewById(R.id.txt1);
            picTimeHolder.txt2 = (TextView)convertView.findViewById(R.id.txt2);
            picTimeHolder.txt3 = (TextView)convertView.findViewById(R.id.txt3);
            // picTimeHolder.gridview = (NoScrollGridView)convertView.findViewById(R.id.timeline_pic);
            convertView.setTag(picTimeHolder);
        }
        else
        {
            picTimeHolder = (PicTimeHolder)convertView.getTag();
        }
        if (mlist.get(position).isFirst())
        {
            picTimeHolder.text_date.setText(new StringBuffer(mlist.get(position).getDate()).insert(2, "/"));
            picTimeHolder.timeline_year.setText(mlist.get(position).getYear() + "年");
            picTimeHolder.text_date.setVisibility(View.VISIBLE);
            picTimeHolder.timeline_year.setVisibility(View.VISIBLE);
            picTimeHolder.timeline_poit.setVisibility(View.VISIBLE);
        }
        else
        {
            picTimeHolder.timeline_poit.setVisibility(View.GONE);
            picTimeHolder.text_date.setVisibility(View.INVISIBLE);
            picTimeHolder.timeline_year.setVisibility(View.INVISIBLE);
        }
        if (mlist.get(position).getPaths().size() == 1)
        {
            Glide.with(context).load(new File(mlist.get(position).getPaths().get(0).getPath() + File.separator + mlist.get(position).getPaths().get(0).getName())).into(picTimeHolder.img1);
            
            picTimeHolder.txt1.setText(getTime(position, 0));
            
            picTimeHolder.txt1.setVisibility(View.VISIBLE);
            picTimeHolder.txt2.setVisibility(View.INVISIBLE);
            picTimeHolder.txt3.setVisibility(View.INVISIBLE);
            
            picTimeHolder.img1.setVisibility(View.VISIBLE);
            picTimeHolder.img2.setVisibility(View.INVISIBLE);
            picTimeHolder.img3.setVisibility(View.INVISIBLE);
        }
        else if (mlist.get(position).getPaths().size() == 2)
        {
            Glide.with(context).load(new File(mlist.get(position).getPaths().get(0).getPath() + File.separator + mlist.get(position).getPaths().get(0).getName())).into(picTimeHolder.img1);
            Glide.with(context).load(new File(mlist.get(position).getPaths().get(1).getPath() + File.separator + mlist.get(position).getPaths().get(1).getName())).into(picTimeHolder.img2);
            picTimeHolder.txt1.setText(getTime(position, 0));
            picTimeHolder.txt2.setText(getTime(position, 1));
            
            picTimeHolder.txt1.setVisibility(View.VISIBLE);
            picTimeHolder.txt2.setVisibility(View.VISIBLE);
            picTimeHolder.txt3.setVisibility(View.INVISIBLE);
            
            picTimeHolder.img1.setVisibility(View.VISIBLE);
            picTimeHolder.img2.setVisibility(View.VISIBLE);
            picTimeHolder.img3.setVisibility(View.INVISIBLE);
        }
        else if (mlist.get(position).getPaths().size() == 3)
        {
            Glide.with(context).load(new File(mlist.get(position).getPaths().get(0).getPath() + File.separator + mlist.get(position).getPaths().get(0).getName())).into(picTimeHolder.img1);
            Glide.with(context).load(new File(mlist.get(position).getPaths().get(1).getPath() + File.separator + mlist.get(position).getPaths().get(1).getName())).into(picTimeHolder.img2);
            Glide.with(context).load(new File(mlist.get(position).getPaths().get(2).getPath() + File.separator + mlist.get(position).getPaths().get(2).getName())).into(picTimeHolder.img3);
            picTimeHolder.txt1.setText(getTime(position, 0));
            picTimeHolder.txt2.setText(getTime(position, 1));
            picTimeHolder.txt3.setText(getTime(position, 2));
            
            picTimeHolder.txt1.setVisibility(View.VISIBLE);
            picTimeHolder.txt2.setVisibility(View.VISIBLE);
            picTimeHolder.txt3.setVisibility(View.VISIBLE);
            
            picTimeHolder.img1.setVisibility(View.VISIBLE);
            picTimeHolder.img2.setVisibility(View.VISIBLE);
            picTimeHolder.img3.setVisibility(View.VISIBLE);
        }
        
        picTimeHolder.img1.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onCenterPicListener != null)
                {
                    onCenterPicListener.onClick(position, 0, mlist.get(position).getPaths().get(0).getName());
                }
            }
        });
        
        picTimeHolder.img2.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onCenterPicListener != null)
                {
                    onCenterPicListener.onClick(position, 1, mlist.get(position).getPaths().get(1).getName());
                }
            }
        });
        picTimeHolder.img3.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onCenterPicListener != null)
                {
                    onCenterPicListener.onClick(position, 2, mlist.get(position).getPaths().get(2).getName());
                }
            }
        });
        
        return convertView;
    }
    
    public void setImageClickListener(OnCenterPicListener onCenterPicListener)
    {
        this.onCenterPicListener = onCenterPicListener;
    }
    
    public interface OnCenterPicListener
    {
        void onClick(int position, int childPosition, String name);
    }
    
    public String getTime(int position, int index)
    {
        String time1 = mlist.get(position).getPaths().get(index).getTime();
        String daytime = time1.substring(8, 10) + ":" + time1.substring(10, 12);
        
        return daytime;
    }
    
    class PicTimeHolder
    {
        TextView text_date;
        
        TextView timeline_year;
        
        ImageView timeline_poit;
        
        // NoScrollGridView gridview;
        
        ImageView img1;
        
        ImageView img2;
        
        ImageView img3;
        
        TextView txt1;
        
        TextView txt2;
        
        TextView txt3;
        
    }
    
}
