package com.vomont.yundudao.bean;

import java.io.Serializable;

public class ProblemInfo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5462375850275661413L;

	private int problemtypeid;
	
	private int relateddeviceid;
	
	private long donetime;
	
	private int ownerid;
	
	private String ccuserids;
	
	private String problemdesp;
	
	private int imagetype;
	
	private byte[] imagecontent;

	public int getProblemtypeid() {
		return problemtypeid;
	}

	public void setProblemtypeid(int problemtypeid) {
		this.problemtypeid = problemtypeid;
	}

	public int getRelateddeviceid() {
		return relateddeviceid;
	}

	public void setRelateddeviceid(int relateddeviceid) {
		this.relateddeviceid = relateddeviceid;
	}

	public long getDonetime() {
		return donetime;
	}

	public void setDonetime(long donetime) {
		this.donetime = donetime;
	}

	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}

	public String getCcuserids() {
		return ccuserids;
	}

	public void setCcuserids(String ccuserids) {
		this.ccuserids = ccuserids;
	}

	public String getProblemdesp() {
		return problemdesp;
	}

	public void setProblemdesp(String problemdesp) {
		this.problemdesp = problemdesp;
	}

	public int getImagetype() {
		return imagetype;
	}

	public void setImagetype(int imagetype) {
		this.imagetype = imagetype;
	}

	public byte[] getImagecontent() {
		return imagecontent;
	}

	public void setImagecontent(byte[] imagecontent) {
		this.imagecontent = imagecontent;
	}
	
}
