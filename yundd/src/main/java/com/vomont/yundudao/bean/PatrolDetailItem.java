package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class PatrolDetailItem implements Serializable{
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4391352260043848602L;

    private long  createtime;

	private int ownersubfactid;
	
	private int uploaderid;
	
	private String uploadername;
	
	private long uploadtime;
	
	private String videodesp;
	
	private int videoid;
	
	private int videolength;
	
	private String videoplayurl;
	
	private List<Vieweraccounts> vieweraccounts;

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public int getOwnersubfactid() {
		return ownersubfactid;
	}

	public void setOwnersubfactid(int ownersubfactid) {
		this.ownersubfactid = ownersubfactid;
	}

	public int getUploaderid() {
		return uploaderid;
	}

	public void setUploaderid(int uploaderid) {
		this.uploaderid = uploaderid;
	}

	public String getUploadername() {
		return uploadername;
	}

	public void setUploadername(String uploadername) {
		this.uploadername = uploadername;
	}

	public long getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(long uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getVideodesp() {
		return videodesp;
	}

	public void setVideodesp(String videodesp) {
		this.videodesp = videodesp;
	}

	public int getVideoid() {
		return videoid;
	}

	public void setVideoid(int videoid) {
		this.videoid = videoid;
	}

	public int getVideolength() {
		return videolength;
	}

	public void setVideolength(int videolength) {
		this.videolength = videolength;
	}

	public String getVideoplayurl() {
		return videoplayurl;
	}

	public void setVideoplayurl(String videoplayurl) {
		this.videoplayurl = videoplayurl;
	}

	public List<Vieweraccounts> getVieweraccounts() {
		return vieweraccounts;
	}

	public void setVieweraccounts(List<Vieweraccounts> vieweraccounts) {
		this.vieweraccounts = vieweraccounts;
	}
	
}
