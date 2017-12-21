package com.vomont.yundudao.ui.pic.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PreViewAdapater extends PagerAdapter
{
    private List<ImageView> viewList;
    
    public PreViewAdapater(List<ImageView> viewList)
    {
        this.viewList = viewList;
    }
    
    @Override
    public int getCount()
    {
        return viewList.size();
    }
    
    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg1 == arg0;
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(viewList.get(position));
    }
}
