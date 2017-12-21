package com.wmclient.clientsdk;

public class WMMapNodeInfo {
	private int nodeId;
    private int devId;
    private int channelId;
    private String devName;
    private double longitude;
    private double latitude;
 
    public int getNodeId() 
    {
        return nodeId;
    }

    public void setNodeId(int nodeId) 
    {
        this.nodeId = nodeId;
    }
    
    public int getDevId() 
    {
        return devId;
    }

    public void setDevId(int devId) 
    {
        this.devId = devId;
    }

    public double getLongitude() 
    {
        return longitude;
    }
    
    public void setLongitude(double longitude) 
    {
        this.longitude = longitude;
    } 

    public int getChannelId() 
    {
        return channelId;
    }

    public void setChannelId(int channelId) 
    {
        this.channelId = channelId;
    }

    public String getDevName() 
    {
        return devName;
    }

    public void setDevName(String devName) 
    {
        this.devName = devName;
    }

    public double getLatitude() 
    {
        return latitude;
    }

    public void setLatitude(double latitude) 
    {
        this.latitude = latitude;
    }
}
