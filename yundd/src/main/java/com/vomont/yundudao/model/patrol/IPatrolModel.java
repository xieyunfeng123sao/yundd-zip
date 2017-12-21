package com.vomont.yundudao.model.patrol;

import com.vomont.yundudao.model.OnHttpListener;

public interface IPatrolModel {
	void getPatrolList(int userid, String searchdescription, int pageidx,
			OnHttpListener onHttpListener);
	void getPatrolDetail(int userid,int videoid,OnHttpListener onHttpListener);
}
