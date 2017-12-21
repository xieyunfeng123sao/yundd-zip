package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

public class PatrolList implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 6440942546107992592L;
    private int result;
	private List<PatrolListItem> videos;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public List<PatrolListItem> getVideos() {
		return videos;
	}
	public void setVideos(List<PatrolListItem> videos) {
		this.videos = videos;
	}
	
	
	
}
