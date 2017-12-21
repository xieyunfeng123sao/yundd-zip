package com.vomont.yundudao.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FormFactory implements Serializable
{
    private int subfactoryid;
    
    private int status_1;
    
    private int status_2;
    
    private int status_3;

    public int getSubfactoryid()
    {
        return subfactoryid;
    }

    public void setSubfactoryid(int subfactoryid)
    {
        this.subfactoryid = subfactoryid;
    }

    public int getStatus_1()
    {
        return status_1;
    }

    public void setStatus_1(int status_1)
    {
        this.status_1 = status_1;
    }

    public int getStatus_2()
    {
        return status_2;
    }

    public void setStatus_2(int status_2)
    {
        this.status_2 = status_2;
    }

    public int getStatus_3()
    {
        return status_3;
    }

    public void setStatus_3(int status_3)
    {
        this.status_3 = status_3;
    }
    
    
    
}
