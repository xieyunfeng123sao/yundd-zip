package com.vomont.yundudao.bean;

import java.io.Serializable;


/**
 * 
 * @author Administrator xyf
 *  msgid  消息的id
 *  msgtype 消息的類型
 *  msgtitle 消息的标题
 *  msgcontent 消息的正文
 *  msgpubtime 消息的时间
 */
@SuppressWarnings("serial")
public class Message implements Serializable
{
    private int msgid;
    
    private String msgtitle;
    
    private int msgtype;
    
    private String msgcontent;
    
    private long msgpubtime;
    
    public int getMsgid()
    {
        return msgid;
    }
    
    public void setMsgid(int msgid)
    {
        this.msgid = msgid;
    }
    
    public String getMsgtitle()
    {
        return msgtitle;
    }
    
    public void setMsgtitle(String msgtitle)
    {
        this.msgtitle = msgtitle;
    }
    
    public int getMsgtype()
    {
        return msgtype;
    }
    
    public void setMsgtype(int msgtype)
    {
        this.msgtype = msgtype;
    }
    
    public String getMsgcontent()
    {
        return msgcontent;
    }
    
    public void setMsgcontent(String msgcontent)
    {
        this.msgcontent = msgcontent;
    }
    
    public long getMsgpubtime()
    {
        return msgpubtime;
    }
    
    public void setMsgpubtime(long msgpubtime)
    {
        this.msgpubtime = msgpubtime;
    }
}
