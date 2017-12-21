package com.vomont.yundudao.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ActionInfo implements Serializable {

	/**
	 * 
	 */

	private int actionid;

	private int actiontype;

	private int actionresult;

	private int actionaccountid;

	private String actionaccountname;

	private String actiondesp;

	private long actiontime;

	public int getActionid() {
		return actionid;
	}

	public void setActionid(int actionid) {
		this.actionid = actionid;
	}

	public int getActiontype() {
		return actiontype;
	}

	public void setActiontype(int actiontype) {
		this.actiontype = actiontype;
	}

	public int getActionresult() {
		return actionresult;
	}

	public void setActionresult(int actionresult) {
		this.actionresult = actionresult;
	}

	public int getActionaccountid() {
		return actionaccountid;
	}

	public void setActionaccountid(int actionaccountid) {
		this.actionaccountid = actionaccountid;
	}

	public String getActionaccountname() {
		return actionaccountname;
	}

	public void setActionaccountname(String actionaccountname) {
		this.actionaccountname = actionaccountname;
	}

	public String getActiondesp() {
		return actiondesp;
	}

	public void setActiondesp(String actiondesp) {
		this.actiondesp = actiondesp;
	}

	public long getActiontime() {
		return actiontime;
	}

	public void setActiontime(long actiontime) {
		this.actiontime = actiontime;
	}

}
