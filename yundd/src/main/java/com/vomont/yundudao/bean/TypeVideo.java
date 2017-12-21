package com.vomont.yundudao.bean;

import java.util.List;

import com.vomont.yundudao.upload.VideoBean;

public class TypeVideo {

	private String type;
	
	private List<VideoBean> mlist;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<VideoBean> getMlist() {
		return mlist;
	}

	public void setMlist(List<VideoBean> mlist) {
		this.mlist = mlist;
	}
	
	  /** 
     *  获取Item内容 
     *  
     * @param pPosition 
     * @return 
     */  
    public VideoBean getItem(int pPosition) {  
        // Category排在第一位  
        if (pPosition == 0) {  
            return null;  
        } else {  
            return mlist.get(pPosition - 1);  
        }  
    }  
      
    /** 
     * 当前类别Item总数。Category也需要占用一个Item 
     * @return  
     */  
    public int getItemCount() {  
    	
    	if(mlist!=null)
    	{
    		  return mlist.size() + 1;  
    	}
      return 1;
    }  
      
	
	
}
