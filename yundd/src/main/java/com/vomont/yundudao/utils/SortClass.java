package com.vomont.yundudao.utils;

import java.util.Comparator;

import com.vomont.yundudao.upload.VideoBean;

@SuppressWarnings("rawtypes")
public class SortClass implements Comparator
{
    public int compare(Object arg0, Object arg1)
    {
        VideoBean videoBean1 = (VideoBean)arg0;
        VideoBean videoBean2 = (VideoBean)arg1;
        int flag = videoBean2.getName().compareTo(videoBean1.getName());
        return flag;
    }
}