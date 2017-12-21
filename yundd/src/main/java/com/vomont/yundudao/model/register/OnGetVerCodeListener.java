package com.vomont.yundudao.model.register;

import com.vomont.yundudao.bean.RegisterBean;

public interface OnGetVerCodeListener {
	
	void OnSucessGetVerCode(RegisterBean registerBean );
	
	void OnFailGetVerCode();

}
