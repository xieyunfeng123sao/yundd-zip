package com.vomont.yundudao.ui.home.fragment.adapter;

import java.util.List;
import java.util.Map;

import com.vomont.yundudao.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class GridManganerAdapter extends BaseAdapter
{
    
    private List<Map<String, Object>> data_list;
    
    private Context context;
    
    private int height;
    
    public GridManganerAdapter(List<Map<String, Object>> data_list, Context context, int height)
    {
        this.context = context;
        this.data_list = data_list;
        this.height = height;
    }
    
    @Override
    public int getCount()
    {
        return data_list != null ? data_list.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return data_list.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyHolder myHolder = null;
        if (convertView == null)
        {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_manger, null);
            myHolder.img = (ImageView)convertView.findViewById(R.id.manger_img);
            myHolder.text = (TextView)convertView.findViewById(R.id.manger_txt);
            myHolder.no_kaifa=(ImageView) convertView.findViewById(R.id.no_kaifa);
            convertView.setTag(myHolder);
        }
        else
        {
            myHolder = (MyHolder)convertView.getTag();
        }
        if(position!=7&&position!=8)
        {
            myHolder.img.setImageResource((Integer)data_list.get(position).get("image"));
        }
        else
        {
            myHolder.img.setVisibility(View.GONE);   
        }
        myHolder.text.setText((String)data_list.get(position).get("text"));
//        if(position==0||position==1||position==2||position==3||position==5||position==6||position==7||position==8)
//        {
        	myHolder.no_kaifa.setVisibility(View.GONE);
//        }
//        else
//        {
//        	myHolder.no_kaifa.setVisibility(View.VISIBLE);
//        }
        //计算item的高度
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height * 2 / 9);
        convertView.setLayoutParams(params);
        return convertView;
    }
    
    class MyHolder
    {
        ImageView img;
        ImageView no_kaifa;
        
        
        TextView text;
    }
}
