package com.vomont.yundudao.model.verfication;

import com.vomont.yundudao.bean.RegisterBean;

public interface OnVerFicationListener {

	void OnSucess(RegisterBean registerBean);

	void OnFail();
}
