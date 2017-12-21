package com.vomont.yundudao.mvpview.login;

import com.vomont.yundudao.bean.UserInfo;

public interface ILoginView {
	/**
	 * 显示progress
	 */
	void showProgress();
	
	/**
	 * 隐藏progress
	 */
	void hideProgress();
	
	/**
	 * 登录成功
	 * @param userInfo  用户信息
	 */
	void loginSucess(UserInfo userInfo);
	
	/**
	 * 登录失败
	 */
	void loginFail();

}
