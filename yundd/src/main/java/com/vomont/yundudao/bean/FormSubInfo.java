package com.vomont.yundudao.bean;

import java.util.List;

public class FormSubInfo {

	private int subfactoryid;

	private List<FormSubCount> typestatistic;

	private long startTime;

	private long endTime;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getSubfactoryid() {
		return subfactoryid;
	}

	public void setSubfactoryid(int subfactoryid) {
		this.subfactoryid = subfactoryid;
	}

	public List<FormSubCount> getTypestatistic() {
		return typestatistic;
	}

	public void setTypestatistic(List<FormSubCount> typestatistic) {
		this.typestatistic = typestatistic;
	}

}
