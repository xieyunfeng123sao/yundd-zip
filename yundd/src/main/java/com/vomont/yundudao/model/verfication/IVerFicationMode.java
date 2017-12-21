package com.vomont.yundudao.model.verfication;

public interface IVerFicationMode {

	void CodeLogin(String num, String code,
			OnVerFicationListener onVerFicationListener);

}
