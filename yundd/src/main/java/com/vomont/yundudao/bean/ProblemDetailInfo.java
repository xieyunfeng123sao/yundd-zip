package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class ProblemDetailInfo implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 3434406558824109003L;

    /**
	 * 
	 */

	private int problemid;

	private int problemtypeid;

	private String problemtypename;

	private int relateddeviceid;

	private String relateddevicename;

	private long creattime;

	private long donetime;

	private int creatorid;

	private String creatorname;

	private int ownerid;

	private String ownername;

	private int problemstatus;

	private String problemdesp;

	
	private List<ProblemImages> imagecontents;

	private List<CcusersInfo> ccusers;

	private List<ActionInfo> actions;

	private int relatedsubfactoryid;
	
	private List<String> imgeNames;
	
	

    public List<String> getImgeNames()
    {
        return imgeNames;
    }

    public void setImgeNames(List<String> imgeNames)
    {
        this.imgeNames = imgeNames;
    }

    public int getRelatedsubfactoryid()
    {
        return relatedsubfactoryid;
    }

    public void setRelatedsubfactoryid(int relatedsubfactoryid)
    {
        this.relatedsubfactoryid = relatedsubfactoryid;
    }

    public List<ProblemImages> getImagecontents()
    {
        return imagecontents;
    }

    public void setImagecontents(List<ProblemImages> imagecontents)
    {
        this.imagecontents = imagecontents;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

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

	public String getRelateddevicename() {
		return relateddevicename;
	}

	public void setRelateddevicename(String relateddevicename) {
		this.relateddevicename = relateddevicename;
	}

	public long getCreattime() {
		return creattime;
	}

	public void setCreattime(long creattime) {
		this.creattime = creattime;
	}

	public long getDonetime() {
		return donetime;
	}

	public void setDonetime(long donetime) {
		this.donetime = donetime;
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

	public String getProblemdesp() {
		return problemdesp;
	}

	public void setProblemdesp(String problemdesp) {
		this.problemdesp = problemdesp;
	}

	public List<CcusersInfo> getCcusers() {
		return ccusers;
	}

	public void setCcusers(List<CcusersInfo> ccusers) {
		this.ccusers = ccusers;
	}

	public List<ActionInfo> getActions() {
		return actions;
	}

	public void setActions(List<ActionInfo> actions) {
		this.actions = actions;
	}
	
	

}
