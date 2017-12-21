package com.vomont.yundudao.mvpview.ver;

import com.vomont.yundudao.bean.RegisterBean;

public interface IVerFicationView {

	void showProgess();

	void hideProgress();

	void getVerCodeSucess(RegisterBean registerBean);

	void getVerCodeFail();

	void getCodeLoginSucess(RegisterBean registerBean);

	void getCodeLoginFail();
}
