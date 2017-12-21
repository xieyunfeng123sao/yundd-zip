package com.vomont.yundudao.model.login;

public interface ILoginModel {
	
	/**
	 * 登录
	 * @param name 用户名
	 * @param password  密码
	 * @param onLoginListener 回调监听
	 */
	void login(String name,String password,OnLoginListener onLoginListener);

}
