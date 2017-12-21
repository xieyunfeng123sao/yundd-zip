package com.vomont.yundudao.utils;

import com.vomont.yundudao.bean.IPInfo;
import com.vomont.yundudao.bean.UserInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareUtil
{
    private Context context;
    
    public ShareUtil(Context context)
    {
        this.context = context;
    }
    
    public void setShare(String num, String psd, int userid, String veyeuserid, String veyekey, int accountid, String ip, int port, String veyeip, int veyeport, int vfilesvrid)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ljq", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        if (null != num)
        {
            editor.putString("num", num);
        }
        if (null != psd)
        {
            editor.putString("psd", psd);
        }
        if (null != veyeuserid)
        {
            editor.putString("veyeuserid", veyeuserid);
        }
        if (null != veyekey)
        {
            editor.putString("veyekey", veyekey);
        }
        if (null != ip)
        {
            editor.putString("ip", ip);
        }
        
        if (null != veyeip)
        {
            editor.putString("veyesvrip", veyeip);
        }
        if (0 != vfilesvrid)
        {
            editor.putInt("vfilesvrid", vfilesvrid);
        }
        editor.putInt("veyesvrport", veyeport);
        editor.putInt("port", port);
        editor.putInt("accountid", accountid);
        
        editor.putInt("userid", userid);
        editor.commit();
    }
    
    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    public UserInfo getShare()
    {
        SharedPreferences share = context.getSharedPreferences("ljq", Context.MODE_WORLD_READABLE);
        
        String num = share.getString("num", "");
        
        String psd = share.getString("psd", "");
        String veyeuserid = share.getString("veyeuserid", "");
        String veyekey = share.getString("veyekey", "");
        
        int userid = share.getInt("userid", -1);
        int accountid = share.getInt("accountid", -1);
        String veyesvrip = share.getString("veyesvrip", "");
        
        int veyesvrport = share.getInt("veyesvrport", -1);
        
        IPInfo ipInfo = new IPInfo();
        ipInfo.setVfilesvrip(share.getString("ip", ""));
        ipInfo.setVfilesvrport(share.getInt("port", 0));
        ipInfo.setVfilesvrid(share.getInt("vfilesvrid", 0));
        
        UserInfo userInfo = new UserInfo();
        userInfo.setNum(num);
        userInfo.setPassword(psd);
        userInfo.setUser_id(userid);
        userInfo.setVeyeuserid(veyeuserid);
        userInfo.setVeyekey(veyekey);
        userInfo.setVeyesvrip(veyesvrip);
        userInfo.setVeyesvrport(veyesvrport);
        userInfo.setAccountid(accountid);
        userInfo.setVfilesvr(ipInfo);
        return userInfo;
        
    }
    
    public void setVersion(int version)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("version", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        
        editor.putInt("version", version);
        editor.commit();
        
    }
    
    public int getVersion()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("version", Context.MODE_PRIVATE);
        int version = sharedPreferences.getInt("version", 0);
        return version;
    }
    
}
