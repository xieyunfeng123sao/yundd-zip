package com.vomont.yundudao.presenter.register;

import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.model.register.IRegisterMode;
import com.vomont.yundudao.model.register.IRegisterModeImpl;
import com.vomont.yundudao.model.verfication.IVerFicationMode;
import com.vomont.yundudao.model.verfication.IVerFicationModeImpl;
import com.vomont.yundudao.model.verfication.OnVerFicationListener;
import com.vomont.yundudao.mvpview.ver.IVerFicationView;

public class VerFicationPresenter {

	private IVerFicationView iVerFicationView;

	@SuppressWarnings("unused")
	private IRegisterMode iRegisterMode;

	private IVerFicationMode iVerFicationMode;

	public VerFicationPresenter(IVerFicationView iVerFicationView) {
		this.iVerFicationView = iVerFicationView;
		iRegisterMode = new IRegisterModeImpl();
		iVerFicationMode = new IVerFicationModeImpl();
	}

	public void codelogin(String num, String code) {
		iVerFicationView.showProgess();
		iVerFicationMode.CodeLogin(num, code, new OnVerFicationListener() {

			@Override
			public void OnSucess(RegisterBean registerBean) {

				iVerFicationView.getCodeLoginSucess(registerBean);
				iVerFicationView.hideProgress();
			}

			@Override
			public void OnFail() {
				// TODO Auto-generated method stub
				iVerFicationView.getVerCodeFail();
				iVerFicationView.hideProgress();
			}
		});

	}

}
