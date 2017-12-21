package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class SubFormCall implements Serializable
{
    private int result;
    
    private List<SubFormBean> statistic;
    
    public int getResult()
    {
        return result;
    }
    
    public void setResult(int result)
    {
        this.result = result;
    }
    
    public List<SubFormBean> getStatistic()
    {
        return statistic;
    }
    
    public void setStatistic(List<SubFormBean> statistic)
    {
        this.statistic = statistic;
    }
    
}
