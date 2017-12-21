package com.vomont.fileloadsdk;

import java.io.Serializable;

public class FragmentCallBack implements Serializable
{
    private int result;
    
    private int fragmentid;
    
    private String fragmentcontext;
    
    private String fileuri;
    
    private String errorfragement;
    
    public int getResult()
    {
        return result;
    }
    
    public void setResult(int result)
    {
        this.result = result;
    }
    
    public int getFragmentid()
    {
        return fragmentid;
    }
    
    public void setFragmentid(int fragmentid)
    {
        this.fragmentid = fragmentid;
    }
    
    public String getFragmentcontext()
    {
        return fragmentcontext;
    }
    
    public void setFragmentcontext(String fragmentcontext)
    {
        this.fragmentcontext = fragmentcontext;
    }
    
    public String getFileuri()
    {
        return fileuri;
    }
    
    public void setFileuri(String fileuri)
    {
        this.fileuri = fileuri;
    }
    
    public String getErrorfragement()
    {
        return errorfragement;
    }
    
    public void setErrorfragement(String errorfragement)
    {
        this.errorfragement = errorfragement;
    }
}
