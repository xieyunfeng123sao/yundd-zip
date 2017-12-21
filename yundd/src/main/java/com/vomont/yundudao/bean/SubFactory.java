package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;


public class SubFactory  implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3206796592480103291L;
	/*
     * subfactoryname 厂区名称
     */
    private String subfactoryname;
    /*
     * subfactoryid  厂区唯一标识ID
     */
    private int subfactoryid;
    /*
     * SortLetters  
     */
    private String SortLetters;
    
    private int ownerfactoryid;
    
    private int directoraccountid;
    
    private List<DeviceInfo> mlist_device;
    
    
    
    public int getOwnerfactoryid() {
		return ownerfactoryid;
	}

	public void setOwnerfactoryid(int ownerfactoryid) {
		this.ownerfactoryid = ownerfactoryid;
	}

	public int getDirectoraccountid() {
		return directoraccountid;
	}

	public void setDirectoraccountid(int directoraccountid) {
		this.directoraccountid = directoraccountid;
	}

	public List<DeviceInfo> getMlist_device()
    {
        return mlist_device;
    }
    
    public void setMlist_device(List<DeviceInfo> mlist_device)
    {
        this.mlist_device = mlist_device;
    }
    
    public String getSortLetters()
    {
        return SortLetters;
    }
    
    public void setSortLetters(String sortLetters)
    {
        SortLetters = sortLetters;
    }
    
    public String getSubfactoryname()
    {
        return subfactoryname;
    }
    
    public void setSubfactoryname(String subfactoryname)
    {
        this.subfactoryname = subfactoryname;
    }
    
    public int getSubfactoryid()
    {
        return subfactoryid;
    }
    
    public void setSubfactoryid(int subfactoryid)
    {
        this.subfactoryid = subfactoryid;
    }
    
}
