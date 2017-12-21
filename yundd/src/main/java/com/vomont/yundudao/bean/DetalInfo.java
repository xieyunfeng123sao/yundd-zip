package com.vomont.yundudao.bean;

import java.io.Serializable;

public class DetalInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3624053187949560023L;

	private int id;

	private String tel;

	private int role;

	private String name;

	private String email;

	private int dpmid;

	private String privilege;
	
	  /*
     * SortLetters  
     */
    private String SortLetters;
    

	public String getSortLetters() {
		return SortLetters;
	}

	public void setSortLetters(String sortLetters) {
		SortLetters = sortLetters;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getDpmid() {
		return dpmid;
	}

	public void setDpmid(int dpmid) {
		this.dpmid = dpmid;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	
	

}
