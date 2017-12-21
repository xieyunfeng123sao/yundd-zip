package com.vomont.yundudao.presenter.register;

import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.model.register.IRegisterMode;
import com.vomont.yundudao.model.register.IRegisterModeImpl;
import com.vomont.yundudao.model.register.OnGetVerCodeListener;
import com.vomont.yundudao.model.register.OnRegisterListener;
import com.vomont.yundudao.mvpview.register.IRegiseterView;

public class RegisterPresenter {

	private IRegisterMode iRegisterMode;

	private IRegiseterView iRegiseterView;

	public RegisterPresenter(IRegiseterView iRegiseterView) {
		this.iRegiseterView = iRegiseterView;
		iRegisterMode = new IRegisterModeImpl();
	}

	public void getVerCode(String num) {
		iRegiseterView.showProgess();
		iRegisterMode.getVerCode(num, new OnGetVerCodeListener() {

			@Override
			public void OnSucessGetVerCode(RegisterBean registerBean) {
				iRegiseterView.getVerCodeSucess(registerBean);
				iRegiseterView.hideProgress();

			}

			@Override
			public void OnFailGetVerCode() {
				iRegiseterView.getVerCodeFail();
				iRegiseterView.hideProgress();
			}
		});
	}

	public void register(String num, String verCode, String passowrd) {
		iRegiseterView.showProgess();

		iRegisterMode.register(num, verCode, passowrd,
				new OnRegisterListener() {

					@Override
					public void getSucessRegister(RegisterBean registerBean) {
						// TODO Auto-generated method stub
						iRegiseterView.reigsterSucess(registerBean);
						iRegiseterView.hideProgress();
					}

					@Override
					public void getFailRegister() {
						iRegiseterView.registerFail();
						iRegiseterView.hideProgress();
					}
				});

	}

}
