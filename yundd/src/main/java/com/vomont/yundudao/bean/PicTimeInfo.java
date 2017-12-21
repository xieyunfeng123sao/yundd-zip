package com.vomont.yundudao.bean;

import java.io.Serializable;


public class PicTimeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919752708670001887L;

	private String time;
	
	private String name;
	private int factoryid;
	
	private int subfactoryid;
	
	private int deviceid;
	
	private String path;

	private int dealId;
	
	public int getDealId()
    {
        return dealId;
    }


    public void setDealId(int dealId)
    {
        this.dealId = dealId;
    }


    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public int getFactoryid() {
		return factoryid;
	}


	public void setFactoryid(int factoryid) {
		this.factoryid = factoryid;
	}


	public int getSubfactoryid() {
		return subfactoryid;
	}


	public void setSubfactoryid(int subfactoryid) {
		this.subfactoryid = subfactoryid;
	}


	public int getDeviceid() {
		return deviceid;
	}


	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	
	
	

}
