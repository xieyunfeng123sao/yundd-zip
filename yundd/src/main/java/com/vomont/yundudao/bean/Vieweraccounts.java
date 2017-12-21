package com.vomont.yundudao.bean;

import java.io.Serializable;

public class Vieweraccounts implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5671260691481670743L;

    private int accountid;
    
    private String accountname;
    
    public int getAccountid()
    {
        return accountid;
    }
    
    public void setAccountid(int accountid)
    {
        this.accountid = accountid;
    }
    
    public String getAccountname()
    {
        return accountname;
    }
    
    public void setAccountname(String accountname)
    {
        this.accountname = accountname;
    }
    
}
