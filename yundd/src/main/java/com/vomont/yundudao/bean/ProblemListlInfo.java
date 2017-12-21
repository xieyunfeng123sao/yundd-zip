package com.vomont.yundudao.bean;

import java.io.Serializable;

public class ProblemListlInfo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3834717371214531943L;

	private int problemid;
	
	private int problemtypeid;
	
	private String problemtypename;
	
	private int relateddeviceid;
	
	private long creattime;
	
	private long dometime;
	
	private int creatorid;
	
	private String creatorname;
	
	private int ownerid;
	
	private String ownername;
	
	private int problemstatus;

	public int getProblemid() {
		return problemid;
	}

	public void setProblemid(int problemid) {
		this.problemid = problemid;
	}

	public int getProblemtypeid() {
		return problemtypeid;
	}

	public void setProblemtypeid(int problemtypeid) {
		this.problemtypeid = problemtypeid;
	}

	public String getProblemtypename() {
		return problemtypename;
	}

	public void setProblemtypename(String problemtypename) {
		this.problemtypename = problemtypename;
	}

	public int getRelateddeviceid() {
		return relateddeviceid;
	}

	public void setRelateddeviceid(int relateddeviceid) {
		this.relateddeviceid = relateddeviceid;
	}

	public long getCreattime() {
		return creattime;
	}

	public void setCreattime(long creattime) {
		this.creattime = creattime;
	}

	public long getDometime() {
		return dometime;
	}

	public void setDometime(long dometime) {
		this.dometime = dometime;
	}

	public int getCreatorid() {
		return creatorid;
	}

	public void setCreatorid(int creatorid) {
		this.creatorid = creatorid;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public int getProblemstatus() {
		return problemstatus;
	}

	public void setProblemstatus(int problemstatus) {
		this.problemstatus = problemstatus;
	}
	
	
	
}
