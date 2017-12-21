package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class TagInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1505494349305172673L;

	private int  typeid ;
	
	private int ownertypeid;
	
	private String typename;
	
	private List<TagInfo> mlist;
	
	private int color;
	
	

	public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public List<TagInfo> getMlist() {
		return mlist;
	}

	public void setMlist(List<TagInfo> mlist) {
		this.mlist = mlist;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public int getOwnertypeid() {
		return ownertypeid;
	}

	public void setOwnertypeid(int ownertypeid) {
		this.ownertypeid = ownertypeid;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}
	
	
	
}
