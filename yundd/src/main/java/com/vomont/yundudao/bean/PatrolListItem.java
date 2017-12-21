package com.vomont.yundudao.bean;

import java.io.Serializable;

public class PatrolListItem implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 2501448082779238595L;
    
    private int videoid;
    
    private int searchdescription;
    
    private String uploadername;
    
    private int ownersubfactid;
    
    private long uploadtime;
    
    private int imagetype;
    
    private String imagecontent;
    
    private String videodesp;
    
    private int videolength;
    
    private String imageurl;
    
    private String videoplayurl;
    
    
    
    public String getImagecontent()
    {
        return imagecontent;
    }
    
    public void setImagecontent(String imagecontent)
    {
        this.imagecontent = imagecontent;
    }
    
    public int getVideoid()
    {
        return videoid;
    }
    
    public void setVideoid(int videoid)
    {
        this.videoid = videoid;
    }
    
    public int getSearchdescription()
    {
        return searchdescription;
    }
    
    public void setSearchdescription(int searchdescription)
    {
        this.searchdescription = searchdescription;
    }
    
    public String getUploadername()
    {
        return uploadername;
    }
    
    public void setUploadername(String uploadername)
    {
        this.uploadername = uploadername;
    }
    
    public int getOwnersubfactid()
    {
        return ownersubfactid;
    }
    
    public void setOwnersubfactid(int ownersubfactid)
    {
        this.ownersubfactid = ownersubfactid;
    }
    
    public long getUploadtime()
    {
        return uploadtime;
    }
    
    public void setUploadtime(long uploadtime)
    {
        this.uploadtime = uploadtime;
    }
    
    public int getImagetype()
    {
        return imagetype;
    }
    
    public void setImagetype(int imagetype)
    {
        this.imagetype = imagetype;
    }
    
    public String getVideodesp()
    {
        return videodesp;
    }
    
    public void setVideodesp(String videodesp)
    {
        this.videodesp = videodesp;
    }
    
    public int getVideolength()
    {
        return videolength;
    }
    
    public void setVideolength(int videolength)
    {
        this.videolength = videolength;
    }
    
    public String getImageurl()
    {
        return imageurl;
    }
    
    public void setImageurl(String imageurl)
    {
        this.imageurl = imageurl;
    }

    public String getVideoplayurl()
    {
        return videoplayurl;
    }

    public void setVideoplayurl(String videoplayurl)
    {
        this.videoplayurl = videoplayurl;
    }
    
}
