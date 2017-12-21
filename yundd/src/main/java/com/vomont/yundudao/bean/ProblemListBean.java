package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class ProblemListBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1324060383704891917L;

	private int  result;
	
	private List<ProblemListlInfo> problems;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<ProblemListlInfo> getProblems() {
		return problems;
	}

	public void setProblems(List<ProblemListlInfo> problems) {
		this.problems = problems;
	}
	
	
	
}
