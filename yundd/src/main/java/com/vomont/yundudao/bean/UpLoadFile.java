package com.vomont.yundudao.bean;

import java.util.List;

public class UpLoadFile {

	private String canLooker;

	private String path;

	private int fragmentid;

	private String fragmentcontext;

	private long creattime;

	private int isLoad;

	private int isPack;
	
	private String img;
	
	private int subFatoryid;
	
	private String fatoryname;
	
	private String desp;
	

	public int getSubFatoryid() {
		return subFatoryid;
	}

	public void setSubFatoryid(int subFatoryid) {
		this.subFatoryid = subFatoryid;
	}

	public String getFatoryname() {
		return fatoryname;
	}

	public void setFatoryname(String fatoryname) {
		this.fatoryname = fatoryname;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	private List<Integer> uploadList;
	public String getCanLooker() {
		return canLooker;
	}

	public void setCanLooker(String canLooker) {
		this.canLooker = canLooker;
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
