package com.vomont.yundudao.upload;

import java.util.List;

public class VideoFragmentInfo {

	private String path;
	
	private int fragmentid;
	
	private String fragmentcontext;
	
	private long creattime;
	
	private int isLoad;
	
	private int isPack;
	
	
	private int result;
	
	
	private List<Integer> uploadList;
	
	
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<Integer> getUploadList() {
		return uploadList;
	}

	public void setUploadList(List<Integer> uploadList) {
		this.uploadList = uploadList;
	}

	public int getIsPack() {
		return isPack;
	}

	public void setIsPack(int isPack) {
		this.isPack = isPack;
	}



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getFragmentid() {
		return fragmentid;
	}

	public void setFragmentid(int fragmentid) {
		this.fragmentid = fragmentid;
	}


	public String getFragmentcontext() {
		return fragmentcontext;
	}

	public void setFragmentcontext(String fragmentcontext) {
		this.fragmentcontext = fragmentcontext;
	}

	public long getCreattime() {
		return creattime;
	}

	public void setCreattime(long creattime) {
		this.creattime = creattime;
	}

	public int getIsLoad() {
		return isLoad;
	}

	public void setIsLoad(int isLoad) {
		this.isLoad = isLoad;
	}
	
	
	
}
