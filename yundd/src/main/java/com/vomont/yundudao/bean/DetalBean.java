package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class DetalBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8146195602335596777L;

	private int result;
	
	private List<DetalInfo> accounts;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<DetalInfo> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<DetalInfo> accounts) {
		this.accounts = accounts;
	}


	

}
