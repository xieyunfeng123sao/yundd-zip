package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class ProblemTypeBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3214685637714638796L;

	private  int result;
	
	private int update;
	
	private List<ProblemTypeInfo> problemtypes;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}

	public List<ProblemTypeInfo> getProblemtypes() {
		return problemtypes;
	}

	public void setProblemtypes(List<ProblemTypeInfo> problemtypes) {
		this.problemtypes = problemtypes;
	}
	
	
}
