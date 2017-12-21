package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class PicTimeBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6556637233081532977L;

	private String date;
	
	private String time;

	private List<PicTimeInfo> paths;
	
	private String year;

	private boolean isFirst;
	
	
	public boolean isFirst()
    {
        return isFirst;
    }

    public void setFirst(boolean isFirst)
    {
        this.isFirst = isFirst;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	public List<PicTimeInfo> getPaths() {
		return paths;
	}


	public void setPaths(List<PicTimeInfo> paths) {
		this.paths = paths;
	}

	
}
