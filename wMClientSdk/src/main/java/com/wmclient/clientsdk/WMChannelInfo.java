package com.wmclient.clientsdk;

import java.io.Serializable;

public class WMChannelInfo implements Serializable
{
    private int channelId;
    private String channelName;
    private int channelPrivilege;
   

    public int getChannelId() 
    {
        return channelId;
    }


    public void setChannelId(int channelId) 
    {
        this.channelId = channelId;
    }
 

    public String getChannelName() 
    {
        return channelName;
    }


    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }    


    public int getChannelPrivilege() 
    {
        return channelPrivilege;
    }


    public void setChannelPrivilege(int channelPrivilege) 
    {
        this.channelPrivilege = channelPrivilege;
    }
}
