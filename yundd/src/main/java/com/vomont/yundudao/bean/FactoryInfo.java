package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author xyf
 * 
 *         factoryid:工厂唯一标识ID；
 *         subfactory:厂区总数；
 *         devcnt:设备总数；
 *         factoryname:工厂名称；
 *         availpercent:工厂可视率（百分比值，如60表示60%）；
 *         onlinepercent:工厂设备在线率（百分比值，如60表示60%）；
 * 
 */
public class FactoryInfo implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1962555435779165055L;

	private int factoryid;
    
    private String factoryname;
    
//    private int subfactory;
//    
//    private int devcnt;
//    
//    private int availpercent;
//    
//    private int onlinepercent;
    
    
    private List<SubFactory> mlist;

	public int getFactoryid() {
		return factoryid;
	}

	public void setFactoryid(int factoryid) {
		this.factoryid = factoryid;
	}

	public String getFactoryname() {
		return factoryname;
	}

	public void setFactoryname(String factoryname) {
		this.factoryname = factoryname;
	}

	public List<SubFactory> getMlist() {
		return mlist;
	}

	public void setMlist(List<SubFactory> mlist) {
		this.mlist = mlist;
	}
    
    
    
}
