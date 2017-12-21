package com.wmclient.clientsdk;

import java.io.Serializable;

public class WMDeviceInfo implements Serializable
{
	private static final long serialVersionUID = 8344797106285912400L;
	
	private int devGroupId;
	private int devId;
    private int devType;
    private String devName;
    private String macCode;
    private int status;
    private WMChannelInfo channelArr[];
	
	/**
	*获取设备组Id
	*@return 设备组Id
    */   
    public int getDevGroupId() 
    {
        return devGroupId;
    }

	/**
	*设置设备组Id
	*@param devGroupId 设备组Id
    */    
    public void setDevGroupId(int devGroupId) 
    {
        this.devGroupId = devGroupId;
    }	

	/**
	*获取设备Id
	*@return 设备Id
    */   
    public int getDevId() 
    {
        return devId;
    }

	/**
	*设置设备Id
	*@param devId 设备Id
    */    
    public void setDevId(int devId) 
    {
        this.devId = devId;
    }
 
	/**
	*获取设备类型
	*@return 设备类型
    */     
    public int getDevType() 
    {
        return devType;
    }

	/**
	*设置设备类型
	*@param devType 设备类型
    */      
    public void setDevType(int devType) 
    {
        this.devType = devType;
    }    

	/**
	*获取设备名称
	*@return 设备名称
    */     
    public String getDevName() 
    {
        return devName;
    }

	/**
	*设置设备名称
	*@param devName 设备名称
    */     
    public void setDevName(String devName) 
    {
        this.devName = devName;
    }
  
	/**
	*获取设备状态
	*@return 设备状态
    */    
    public int getStatus() 
    {
        return status;
    }

	/**
	*设置设备状态
	*@param status 设备状态
    */     
    public void setStatus(int status) 
    {
        this.status = status;
    }  
 
	/**
	*获取设备通道信息
	*@return 设备通道数组
    */       
    public WMChannelInfo[] getChannelArr()
    {
    	return channelArr;
    }
  
	/**
	*设置设备通道信息
	*@param channelArr 设备通道信息
    */     
    public void setChannelArr(WMChannelInfo[] channelArr)
    {
    	this.channelArr = channelArr;
    }
    
    public String getMacCode() 
    {
        return macCode;
    }

    public void setMacCode(String mMacCode) 
    {
        this.macCode = mMacCode;
    }
}
