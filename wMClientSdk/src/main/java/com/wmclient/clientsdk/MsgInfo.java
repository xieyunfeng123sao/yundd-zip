package com.wmclient.clientsdk;

import java.util.Calendar;
import java.util.Date;

public class MsgInfo {

	public static final String Type_All="Type_All";
	public static final String Type_Alarm="Type_Alarm";
	public static final String Type_Other="Type_Other";
	
	private String type="";
	private String msg="";
	private Calendar date=null;
	
	public MsgInfo() {		
		
	}
	
	public MsgInfo(String type,String msg,Calendar date) {
		this.setType(type);
		this.setMsg(msg);
		this.setDate(date);		
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar calendar) {
		this.date = calendar;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
