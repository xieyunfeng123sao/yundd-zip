package com.vomont.yundudao.conent;

public interface Con_Action
{
	public interface HTTP_PAMRS
    {
        //
        public static String msgid = "msgid";
        
        // 电话
        public static String tel = "tel";
        
        // 验证码
        public static String verifCode = "verifCode";
        
        // 密码
        public static String pswd = "pswd";
        
        // 新密码
        public static String newpswd = "newpswd";
        
        // 用户id
        public static String userid = "userid";
        
        // 原始密码
        public static String oldpswd = "oldpswd";
        // 头像
        public static String iconfile = "iconfile";
        //消息的版本
        public static String msgversion = "msgversion";
        //工厂的id
        public static String factoryid = "factoryid";
    }
    
    
    public interface HTTP_TYPE
    {
        // 获取验证码
        public static String type1 = "257";
        
        // 注册
        public static String type2 = "258";
        
        // 密码登录
        public static String type3 = "259";
        
        // 验证码登录
        public static String type4 = "260";
        
        // 登出
        public static String type5 = "262";
        
        // 登出
        public static String type6 = "261";
        
        // 修改密码
        public static String type7 = "275";
        
        // 修改个人头像
        public static String type8 = "264";
        
        // 个人头像
        public static String type9 = "263";
        
        // 获取新系统通知列表
        public static String type10 = "288";
        
        // 获取工厂列表
        public static String type11 = "368";
        
        
        // 获取所有厂区的列表
        
        public static String type12 = "305";
        
        // 获取某个工厂的所有设备列表
        
        public static String type13 = "306";
        
    }
    
    // 注册界面 RegisterActivity
    public static String REGISTER_ACTION = "android.intent.action.registeractivity";
    
    // 获取验证码界面 RegisterNumActivity
    public static String REGISTERNUM_ACTION = "android.intent.action.registernumactivity";
    
    // 验证码界面 VerificationActivity
    public static String VERIFICATION_ACTION = "android.intent.action.verificationactivity";
    
    // 重置密码界面 NewPsdActivity
    public static String NEWPSD_ACTION = "android.intent.action.newpsdactivity";
    
    // 主界面 homeactivity
    public static String HOME_ACTION = "android.intent.action.homeactivity";
    
    // 修改密碼界面
    public static String CHANGE_PSD_ACTION = "android.intent.action.changepsdactivity";
    
    // 系统设置界面
    public static String SETTING_ACTION = "android.intent.action.settingactivity";
    
    // 新消息通知界面
    public static String SETTING_MSG_ACTION = "android.intent.action.settingmsgactivity";
    
    // 关于我们
    public static String ABOUT_ACTION = "android.intent.action.aboutactivity";
    
    //消息列表
    public static String MSG_ACTION = "android.intent.action.msgListactivity";
    
    //工厂列表界面
    public static String FACTORY_ACTION = "android.intent.action.factoryactivity";
    //消息详情
    public static String MSG_ALL_ACTION = "android.intent.action.msgallactivity";
    //工厂播放界面
    public static String VEDIO_ACTION = "android.intent.action.vediofactoryactivity";
    
    
    public interface Msg
    {
    	public static int  GET_FACTORY_SUCESS=0x000000;
    	public static int  GET_FACTORY_FAIL=0x000001;
    	
    	public static int  GET_SUBFACTORY_SUCESS=0x000002;
    	public static int  GET_SUBFACTORY_FAIL=0x000003;
    	
    	public static int  GET_DEVICE_SUCESS=0x000004;
    	public static int  GET_DEVICE_FAIL=0x000005;
    }
}
