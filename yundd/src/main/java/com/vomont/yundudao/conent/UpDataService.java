package com.vomont.yundudao.conent;

import com.vomont.yundudao.bean.UserInfo;
import com.vomont.yundudao.model.login.ILoginModeImpl;
import com.vomont.yundudao.model.login.OnLoginListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpDataService extends Service {

	@SuppressWarnings("unused")
	private String fileName;

	@SuppressWarnings("unused")
	private UpLoadCallBak callBak;

	private ILoginModeImpl iLoginModeImpl;

	public UpDataService(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		iLoginModeImpl = new ILoginModeImpl();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	/**
	 * 开始上传文件
	 */
	public void onStartUpLoad() {
		for (int i = 0; i < 5; i++)
			iLoginModeImpl.login("1232131231", "123213124124",
					new OnLoginListener() {

						@Override
						public void OnSucess(UserInfo userInfo) {
							Log.d("insert", userInfo.getNum() + "========");
						}

						@Override
						public void OnFail() {
							Log.d("insert", "=======");
						}
					});

	}

	public interface UpLoadCallBak {
		void onLoading(int i, int max);

		void onSucess();
	}

	/**
	 * 上传的回调监听
	 * 
	 * @param callBak
	 */
	public void setUpLoadListener(UpLoadCallBak callBak) {
		this.callBak = callBak;
	}

}
