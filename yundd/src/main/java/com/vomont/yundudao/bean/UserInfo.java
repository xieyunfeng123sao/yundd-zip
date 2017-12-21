package com.vomont.yundudao.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4546100392520584447L;

	private int result;

	private String num;

	private String password;

	private int userid;

	private String veyeuserid;

	private String veyekey;

	private int accountid;
	
	private String veyesvrip;
	
	
	private int veyesvrport;
	
	
	private IPInfo vfilesvr;

	
	
	
	
	public String getVeyesvrip()
    {
        return veyesvrip;
    }

    public void setVeyesvrip(String veyesvrip)
    {
        this.veyesvrip = veyesvrip;
    }

    public int getVeyesvrport()
    {
        return veyesvrport;
    }

    public void setVeyesvrport(int veyesvrport)
    {
        this.veyesvrport = veyesvrport;
    }

    public IPInfo getVfilesvr() {
		return vfilesvr;
	}

	public void setVfilesvr(IPInfo vfilesvr) {
		this.vfilesvr = vfilesvr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getVeyeuserid() {
		return veyeuserid;
	}

	public void setVeyeuserid(String veyeuserid) {
		this.veyeuserid = veyeuserid;
	}

	public String getVeyekey() {
		return veyekey;
	}

	public void setVeyekey(String veyekey) {
		this.veyekey = veyekey;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUser_id() {
		return userid;
	}

	public void setUser_id(int userid) {
		this.userid = userid;
	}

}
