package com.vomont.yundudao.model.login;

import com.vomont.yundudao.bean.UserInfo;

public interface OnLoginListener {

	void OnSucess(UserInfo userInfo);

	void OnFail();

}
