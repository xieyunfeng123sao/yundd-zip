package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class SubFormBean implements Serializable
{
    private int subfactoryid;
    
    private List<SubFormCount> problems;
    
    public int getSubfactoryid()
    {
        return subfactoryid;
    }
    
    public void setSubfactoryid(int subfactoryid)
    {
        this.subfactoryid = subfactoryid;
    }
    
    public List<SubFormCount> getProblems()
    {
        return problems;
    }
    
    public void setProblems(List<SubFormCount> problems)
    {
        this.problems = problems;
    }
    
}
