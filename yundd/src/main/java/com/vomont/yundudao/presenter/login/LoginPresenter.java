package com.vomont.yundudao.presenter.login;

import com.vomont.yundudao.bean.UserInfo;
import com.vomont.yundudao.model.login.ILoginModeImpl;
import com.vomont.yundudao.model.login.ILoginModel;
import com.vomont.yundudao.model.login.OnLoginListener;
import com.vomont.yundudao.mvpview.login.ILoginView;

public class LoginPresenter {

	private ILoginView iLoginView;

	private ILoginModel iLoginModel;

	public LoginPresenter(ILoginView iLoginView) {
		this.iLoginView = iLoginView;
		iLoginModel = new ILoginModeImpl();
	}
	
	/**
	 *  登录 
	 * @param name  用户名
	 * @param password  密码
	 */
	public void login(String name, String password) {
		iLoginView.showProgress();
		iLoginModel.login(name, password, new OnLoginListener() {

			@Override
			public void OnSucess(UserInfo userInfo) {
				iLoginView.loginSucess(userInfo);
				iLoginView.hideProgress();
			}

			@Override
			public void OnFail() {
				iLoginView.loginFail();
				iLoginView.hideProgress();

			}
		});
	}

}
