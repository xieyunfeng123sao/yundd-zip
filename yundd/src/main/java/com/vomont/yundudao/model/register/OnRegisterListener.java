package com.vomont.yundudao.model.register;

import com.vomont.yundudao.bean.RegisterBean;

public interface OnRegisterListener {
	
	void getSucessRegister(RegisterBean registerBean);
	
	void getFailRegister();

}
