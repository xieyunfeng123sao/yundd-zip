package com.vomont.yundudao.model.register;

public interface IRegisterMode {
	
	void  getVerCode(String num,OnGetVerCodeListener onRegisterListener);
	void  register(String num,String verCode,String password,OnRegisterListener onRegisterListener);
}
