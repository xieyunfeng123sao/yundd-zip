package com.wmclient.clientsdk;

public class FileDuration 
{
	private WM_TimeVal m_tvBeginTime;
	private WM_TimeVal m_tvEndTime;
	
    public WM_TimeVal getBeginTime() 
    {
        return m_tvBeginTime;
    }

    public void setBeginTime(WM_TimeVal beginTime) 
    {
        this.m_tvBeginTime = beginTime;
    }	
    
    public WM_TimeVal getEndTime() 
    {
        return m_tvEndTime;
    }

    public void setEndTime(WM_TimeVal endTime) 
    {
        this.m_tvEndTime = endTime;
    }    
}
