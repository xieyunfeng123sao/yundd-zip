package com.vomont.yundudao.bean;

import java.util.List;

public class NewProblemBean
{
    private int problemid;
    
    private int problemtypeid;
    
    private String problemtypename;
    
    private int sourcetype;
    
    private int relateddeviceid;
    
    private int relatedsubfactoryid;
    
    private long createtime;
    
    private long donetime;
    
    private int creatorid;
    
    private String creatorname;
    
    private int ownerid;
    
    private String ownername;
    //09-19 14:55:16.197: E/callback(28156):          "createtime" : 1504952046,


    private int  problemstatus;
    
    private String problemdesp;
    
    private List<NewProblemImg> images;
    private List<CcusersInfo> ccusers;
    
    private List<ActionInfo> actions;

    public int getProblemid()
    {
        return problemid;
    }

    public void setProblemid(int problemid)
    {
        this.problemid = problemid;
    }

    public int getProblemtypeid()
    {
        return problemtypeid;
    }

    public void setProblemtypeid(int problemtypeid)
    {
        this.problemtypeid = problemtypeid;
    }

    public String getProblemtypename()
    {
        return problemtypename;
    }

    public void setProblemtypename(String problemtypename)
    {
        this.problemtypename = problemtypename;
    }

    public int getSourcetype()
    {
        return sourcetype;
    }

    public void setSourcetype(int sourcetype)
    {
        this.sourcetype = sourcetype;
    }

    public int getRelateddeviceid()
    {
        return relateddeviceid;
    }

    public void setRelateddeviceid(int relateddeviceid)
    {
        this.relateddeviceid = relateddeviceid;
    }

    public int getRelatedsubfactoryid()
    {
        return relatedsubfactoryid;
    }

    public void setRelatedsubfactoryid(int relatedsubfactoryid)
    {
        this.relatedsubfactoryid = relatedsubfactoryid;
    }

    public int getCreatorid()
    {
        return creatorid;
    }

    public void setCreatorid(int creatorid)
    {
        this.creatorid = creatorid;
    }

    public String getCreatorname()
    {
        return creatorname;
    }

    public void setCreatorname(String creatorname)
    {
        this.creatorname = creatorname;
    }

    public int getOwnerid()
    {
        return ownerid;
    }

    public void setOwnerid(int ownerid)
    {
        this.ownerid = ownerid;
    }

    public String getOwnername()
    {
        return ownername;
    }

    public void setOwnername(String ownername)
    {
        this.ownername = ownername;
    }

    public int getProblemstatus()
    {
        return problemstatus;
    }

    public void setProblemstatus(int problemstatus)
    {
        this.problemstatus = problemstatus;
    }

    public String getProblemdesp()
    {
        return problemdesp;
    }

    public void setProblemdesp(String problemdesp)
    {
        this.problemdesp = problemdesp;
    }

    public List<NewProblemImg> getImages()
    {
        return images;
    }

    public void setImages(List<NewProblemImg> images)
    {
        this.images = images;
    }

    public List<CcusersInfo> getCcusers()
    {
        return ccusers;
    }

    public void setCcusers(List<CcusersInfo> ccusers)
    {
        this.ccusers = ccusers;
    }

    public List<ActionInfo> getActions()
    {
        return actions;
    }

    public void setActions(List<ActionInfo> actions)
    {
        this.actions = actions;
    }

    public long getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(long createtime)
    {
        this.createtime = createtime;
    }

    public long getDonetime()
    {
        return donetime;
    }

    public void setDonetime(long donetime)
    {
        this.donetime = donetime;
    }
    
    
    
}
