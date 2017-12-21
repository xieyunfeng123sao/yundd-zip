package com.vomont.yundudao.bean;

import java.util.List;

public class TagBean {
	
	private String result;
	
	private int update;
	
	private List<TagInfo> problemtypes;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}

	public List<TagInfo> getProblemtypes() {
		return problemtypes;
	}

	public void setProblemtypes(List<TagInfo> problemtypes) {
		this.problemtypes = problemtypes;
	}
	
	

}
