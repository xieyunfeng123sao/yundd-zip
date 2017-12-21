package com.vomont.yundudao.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MsgBean implements Serializable
{
    
    private int result;
    
    private int msgversion;
    
    private List<Message> message;
    
    public int getResult()
    {
        return result;
    }
    
    public void setResult(int result)
    {
        this.result = result;
    }
    
    public int getMsgversion()
    {
        return msgversion;
    }
    
    public void setMsgversion(int msgversion)
    {
        this.msgversion = msgversion;
    }
    
    public List<Message> getMessage()
    {
        return message;
    }
    
    public void setMessage(List<Message> message)
    {
        this.message = message;
    }
    
}
