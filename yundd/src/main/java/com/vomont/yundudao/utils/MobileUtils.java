package com.vomont.yundudao.utils;


public class MobileUtils
{
    
    /**
     * 
     * 
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles)
    {
        if (mobiles.length() == 11)
        {
            return true;
        }
        else
        {
            return false;
        }
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        
//        Matcher m = p.matcher(mobiles);
//        
//        return m.matches();
        
    }
}
