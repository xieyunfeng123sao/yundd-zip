package com.wmclient.clientsdk;

import java.io.Serializable;

public class WMDeviceGroup implements Serializable
{
    private int groupId;
    private int fatherGroupId;   
	private String groupName;


    public int getGroupId() 
    {
        return groupId;
    }


    public void setGroupId(int groupId) 
    {
        this.groupId = groupId;
    }
	

    public int getFatherGroupId() 
    {
        return fatherGroupId;
    }


    public void setFatherGroupId(int fatherGroupId) 
    {
        this.fatherGroupId = fatherGroupId;
    }	

    public String getGroupName() 
    {
        return groupName;
    }


    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }    
}
