package com.vomont.yundudao.bean;

import java.io.Serializable;

public class CcusersInfo implements Serializable {

	
	/**
     * 
     */
    private static final long serialVersionUID = 9136965288591189641L;

    /**
	 * 
	 */

	private int accountid;
	
	private String accountname;

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	
	
}
