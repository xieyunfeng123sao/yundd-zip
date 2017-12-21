package com.vomont.yundudao.bean;
/**
 * 
 * @author Administrator xyf
 *  hasRegister  是否註冊
 *  verifCode    验证码
 *  userid       用户id
 *  veyeuserid    veye用户的id
 *  veyekey       veye的key
 */
public class RegisterBean
{
    int hasRegister;
    
    int result;
    
    String verifCode;
    
    int userid;
    
    String iconData;
    
    private String veyeuserid;
    
    private String veyekey;
    
    private int accountid;
    
    private String veyesvrip;
    
    
    private int veyesvrport;
    
    
    
    public String getVeyesvrip()
    {
        return veyesvrip;
    }

    public void setVeyesvrip(String veyesvrip)
    {
        this.veyesvrip = veyesvrip;
    }

    public int getVeyesvrport()
    {
        return veyesvrport;
    }

    public void setVeyesvrport(int veyesvrport)
    {
        this.veyesvrport = veyesvrport;
    }

    private IPInfo vfilesvr;
    
    
    public IPInfo getVfilesvr() {
		return vfilesvr;
	}

	public void setVfilesvr(IPInfo vfilesvr) {
		this.vfilesvr = vfilesvr;
	}

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public String getVeyeuserid()
    {
        return veyeuserid;
    }
    
    public void setVeyeuserid(String veyeuserid)
    {
        this.veyeuserid = veyeuserid;
    }
    
    public String getVeyekey()
    {
        return veyekey;
    }
    
    public void setVeyekey(String veyekey)
    {
        this.veyekey = veyekey;
    }
    
    public String getIconData()
    {
        return iconData;
    }
    
    public void setIconData(String iconData)
    {
        this.iconData = iconData;
    }
    
    public int getUserid()
    {
        return userid;
    }
    
    public void setUserid(int userid)
    {
        this.userid = userid;
    }
    
    public int getHasRegister()
    {
        return hasRegister;
    }
    
    public void setHasRegister(int hasRegister)
    {
        this.hasRegister = hasRegister;
    }
    
    public int getResult()
    {
        return result;
    }
    
    public void setResult(int result)
    {
        this.result = result;
    }
    
    public String getVerifCode()
    {
        return verifCode;
    }
    
    public void setVerifCode(String verifCode)
    {
        this.verifCode = verifCode;
    }
    
}
