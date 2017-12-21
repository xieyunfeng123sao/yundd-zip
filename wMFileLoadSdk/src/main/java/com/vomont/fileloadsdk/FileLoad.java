package com.vomont.fileloadsdk;

import java.io.Serializable;
import java.util.HashMap;

public class FileLoad implements Serializable
{
    //文件路径
    private String path;
    
    private String name;
    
    // 文件标志id
    private int fileIdx;
    
    // 上传分片的position以及返回的context
    private HashMap<Integer, String> map;
    
    //是否打包
    private boolean hasPack;
    
    //上传的状态
    private int loadState;
    
    // 文件上传成功后返回的url
    private String fileUri;
    
    
    public String getPath()
    {
        return path;
    }
    
    public void setPath(String path)
    {
        this.path = path;
    }
    
    public int getFileIdx()
    {
        return fileIdx;
    }
    
    public void setFileIdx(int fileIdx)
    {
        this.fileIdx = fileIdx;
    }
    
    public HashMap<Integer, String> getMap()
    {
        return map;
    }
    
    public void setMap(HashMap<Integer, String> map)
    {
        this.map = map;
    }
    
    public int getLoadState()
    {
        return loadState;
    }
    
    public void setLoadState(int loadState)
    {
        this.loadState = loadState;
    }
    
    public String getFileUri()
    {
        return fileUri;
    }
    
    public void setFileUri(String fileUri)
    {
        this.fileUri = fileUri;
    }

    public boolean isHasPack()
    {
        return hasPack;
    }

    public void setHasPack(boolean hasPack)
    {
        this.hasPack = hasPack;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
}
