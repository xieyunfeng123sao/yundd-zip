package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class ManualBean implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 4829034553759479469L;
    private int id;
    private String name;
    
    private int length;
    
    private int mode;
    
    private String devIdList;
    
    private List<DeviceInfo> devList;
    

    public String getDeviceIdList()
    {
        return devIdList;
    }

    public void setDeviceIdList(String devIdList)
    {
        this.devIdList = devIdList;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public int getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public List<DeviceInfo> getDevList()
    {
        return devList;
    }

    public void setDevList(List<DeviceInfo> devList)
    {
        this.devList = devList;
    }
    
}
