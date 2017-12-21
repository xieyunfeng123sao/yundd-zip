package com.vomont.yundudao.bean;

import java.util.List;

public class FormBean
{
    private int result;
    
    private List<FormFactory> statusstatistic;
    
    private List<FormType>  typestatistic;
    

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public List<FormFactory> getStatusstatistic()
    {
        return statusstatistic;
    }

    public void setStatusstatistic(List<FormFactory> statusstatistic)
    {
        this.statusstatistic = statusstatistic;
    }

    public List<FormType> getTypestatistic()
    {
        return typestatistic;
    }

    public void setTypestatistic(List<FormType> typestatistic)
    {
        this.typestatistic = typestatistic;
    }
    
    
    
}
