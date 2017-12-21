package com.vomont.yundudao.ui.factory.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 回放和实时播放的切换
 * 
 * @author Administrator xyf
 * 
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter
{
    
    ArrayList<Fragment> list;
    
    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list)
    {
        super(fm);
        this.list = list;
    }
    
    @Override
    public int getCount()
    {
        return list.size();
    }
    
    @Override
    public Fragment getItem(int arg0)
    {
        return list.get(arg0);
    }
    
}
