package com.vomont.yundudao.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FormType implements Serializable
{
    private int typeid;
    
    private String typename;
    
    private int problemcnt;

    public int getTypeid()
    {
        return typeid;
    }

    public void setTypeid(int typeid)
    {
        this.typeid = typeid;
    }

    public String getTypename()
    {
        return typename;
    }

    public void setTypename(String typename)
    {
        this.typename = typename;
    }

    public int getProblemcnt()
    {
        return problemcnt;
    }

    public void setProblemcnt(int problemcnt)
    {
        this.problemcnt = problemcnt;
    }
    
    
    
}
