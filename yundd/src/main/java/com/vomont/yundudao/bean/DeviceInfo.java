package com.vomont.yundudao.bean;

import java.io.Serializable;

/**
 * deviceid:设备唯一标识ID；
 * devchannelid:设备通道ID；
 * streamsvrid:流媒体服务器ID；
 * devicename:厂区名称；
 * ownerid:所属厂区ID；
 * status：设备在线状态(0:不在线，1:在线)；
 * privilege：设备访问权限(0:不可访问，1:可访问)；
 * 
 * @author Administrator
 */

public class DeviceInfo implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2319087883292099308L;

	private int deviceid;
    
    private String devicename;
    
    private int hasright;
    
    private int online;
    
    private int ptz;
    
    private int subfactoryid;
    
    private int veyechannel;
    
    private int veyeid;
    
    private int voicetalk;

	public int getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public int getHasright() {
		return hasright;
	}

	public void setHasright(int hasright) {
		this.hasright = hasright;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getPtz() {
		return ptz;
	}

	public void setPtz(int ptz) {
		this.ptz = ptz;
	}

	public int getSubfactoryid() {
		return subfactoryid;
	}

	public void setSubfactoryid(int subfactoryid) {
		this.subfactoryid = subfactoryid;
	}

	public int getVeyechannel() {
		return veyechannel;
	}

	public void setVeyechannel(int veyechannel) {
		this.veyechannel = veyechannel;
	}

	public int getVeyeid() {
		return veyeid;
	}

	public void setVeyeid(int veyeid) {
		this.veyeid = veyeid;
	}

	public int getVoicetalk() {
		return voicetalk;
	}

	public void setVoicetalk(int voicetalk) {
		this.voicetalk = voicetalk;
	}
    
    
    
    
 
    
}
