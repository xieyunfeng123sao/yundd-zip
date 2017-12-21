package com.wmclient.clientsdk;

public class WMFileInfo {
	private long startTime;
    private long endTime;
    private long fileSize;
    private String url;

  
    public long getStartTime() 
    {
        return startTime;
    }
   
    public void setStartTime(long startTime) 
    {
        this.startTime = startTime;
    }
 
    public long getEndTime() 
    {
        return endTime;
    }
    
    public void setEndTime(long endTime) 
    {
        this.endTime = endTime;
    }  
    
    public void setFileSize(long fileSize) 
    {
        this.fileSize = fileSize;
    }  
    
    public long getFileSize() 
    {
        return fileSize;
    }
 
    public String getUrl() 
    {
        return url;
    }
    
    public void setUrl(String url) 
    {
        this.url = url;
    }
}
