package com.vomont.yundudao.base64;

public class CEStreamExhausted extends RuntimeException
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4204221159023721893L;
    
    /**
     * <默认构造函数>
     */
    public CEStreamExhausted()
    {
        super();
    }
    
    /**
     * <默认构造函数>
     */
    public CEStreamExhausted(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }
    
    /**
     * <默认构造函数>
     */
    public CEStreamExhausted(String detailMessage)
    {
        super(detailMessage);
    }
    
    /**
     * <默认构造函数>
     */
    public CEStreamExhausted(Throwable throwable)
    {
        super(throwable);
    }
    
}
