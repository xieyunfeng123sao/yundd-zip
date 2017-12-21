package com.vomont.yundudao.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SubFormCount implements Serializable
{
    private int typeid;
    
    private int count;
    
    public int getTypeid()
    {
        return typeid;
    }
    
    public void setTypeid(int typeid)
    {
        this.typeid = typeid;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
}
