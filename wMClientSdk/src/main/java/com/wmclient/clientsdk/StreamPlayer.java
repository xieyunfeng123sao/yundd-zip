package com.wmclient.clientsdk;

import com.wmclient.clientsdk.WMClientSdk.OnHasRealDataCallBackListener;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class StreamPlayer implements RealPlayCallBack, BackPlayCallBack {
	private int iFlag = 0;
	private int m_deviceType = 0;

	private IPlayer m_player = null;
	private Object m_showObj = null;

	private int m_codeRate = 0;

	private long m_lastTimestamp = 0;
	private long m_recvLen = 0;
	private long m_startTimestamp = 0;
	private long m_allRecvLen = 0;
	private static final int StatisticInterval = 2 * 1000; // 2s

	private OnStreamPlayerListener listener;
	private boolean notified = false;
	private boolean bHeadHasCome = false;

	private OnHasRealDataCallBackListener mHasRealDataCallBackListener = null;

	// test
	private long m_startTime = 0;

	public interface OnStreamPlayerListener {
		void onStart();

		void onSuccess();

		void onFailed();
	}

	public void setOnStreamPlayerListener(OnStreamPlayerListener listener) {
		this.listener = listener;
	}

	public StreamPlayer(int deviceType) {
		switch (deviceType) {
		case Constants.DEVICE_TYPE_HK_DEV:
		case Constants.DEVICE_TYPE_HK_PUSHDEV: {
			m_player = new HKPlayer();
		}
			break;

		case Constants.DEVICE_TYPE_XM_DEV: {
			m_player = new XMPlayer();
		}
			break;

		case Constants.DEVICE_TYPE_VEYE_DEV: 
		{
			m_player = new VeyePlayer();
		}
		break;
		
		case Constants.DEVICE_TYPE_RTSP_DEV: 
		case Constants.DEVICE_TYPE_ONVIF_DEV: 
		{
			m_player = new RtspPlayer();
		}
		break;

		default:
			break;
		}

		m_deviceType = deviceType;
	}

	public int getDeviceType() {
		return m_deviceType;
	}

	public void setShowObj(Object showObj, int streamType) {
		if (m_player == null) {
			return;
		}

		m_showObj = showObj;

		if (Constants.DEVICE_TYPE_HK_DEV != m_deviceType
				&& Constants.DEVICE_TYPE_HK_PUSHDEV != m_deviceType) {
			m_player.StartPlay(null, 0, streamType, m_showObj);

			// reset
			m_lastTimestamp = 0;
			m_recvLen = 0;

			m_startTimestamp = 0;
			m_allRecvLen = 0;
		}

		iFlag = 0;
		bHeadHasCome = false;
	}

	public void stopPlay() {
		if (m_player == null) {
			return;
		}

		if (Constants.success != m_player.StopPlay()) {
			Log.e("clientsdk.jar", "stop play fail.");
		} else {
			m_player = null;
			listener = null;
		}
	}

	public void fRealDataCallBack(int iRealHandle, int iDataType,
			byte[] pDataBuffer, int iDataSize) {
		// call back
		if (iFlag <= 0 && null != mHasRealDataCallBackListener) {
			mHasRealDataCallBackListener.OnHasRealDataCallBack();
		}

		iFlag++;

		Log.d("StreamPlayer", "" + iDataType + " " + iDataSize);

		if (null == m_player) {
			notifyFailed();
			DebugLogger.i("StreamPlayer: null" + m_player);
			return;
		}

		// start play
		if (!m_player.IsPlaying()) {
			DebugLogger.i("StreamPlayer: !IsPlaying" + m_player);
			if (Constants.DEVICE_TYPE_HK_DEV != m_deviceType
					&& Constants.DEVICE_TYPE_HK_PUSHDEV != m_deviceType) {
				if (Constants.StreamDataType_Head != iDataType
						&& Constants.success != m_player.StartPlay(null, 0,
								Constants.WMStreamType_RealTime, m_showObj)) {
					notifyFailed();
					Log.e("clientsdk.jar", "start play fail, no head.");
					return;
				}
			} else {
				// open stream
				if (Constants.StreamDataType_Head == iDataType
						&& Constants.success != m_player.StartPlay(pDataBuffer,
								iDataSize, Constants.WMStreamType_RealTime,
								m_showObj)) {
					notifyFailed();
					Log.e("clientsdk.jar", "start play fail, headsize:"
							+ iDataSize);
					return;
				}
			}
		} else {
			if ((Constants.DEVICE_TYPE_HK_DEV == m_deviceType || Constants.DEVICE_TYPE_HK_PUSHDEV == m_deviceType)
					&& Constants.StreamDataType_Head == iDataType) {
				return;
			}

			notifySuccess();
			m_player.InputData(pDataBuffer, iDataSize);
		}

		long curTimestamp = System.currentTimeMillis();
		if (m_lastTimestamp == 0) {
			m_lastTimestamp = curTimestamp;
			m_startTimestamp = curTimestamp;
		}

		// add size
		m_recvLen += iDataSize;
		m_allRecvLen += iDataSize;

		// code rate
		long interval = curTimestamp - m_lastTimestamp;
		if (interval >= StatisticInterval) {
			m_codeRate = (int) (m_recvLen / (interval / 1000) / 1024);

			// reset
			m_lastTimestamp = curTimestamp;
			m_recvLen = 0;
		}

	}

	private void notifyFailed() {
		if (listener != null) {
			listener.onFailed();
		}
	}

	private void notifySuccess() {
		if (listener != null) {
			listener.onSuccess();
		}
	}

	public int saveSnapshot(String fileName) {
		if (null == m_player) {
			return Constants.fail;
		}

		return m_player.SaveSnapshot(fileName + ".jpg",
				Constants.WMSnapshotType_JPEG);
	}

	public int OpenSound() {
		if (null == m_player) {
			return Constants.fail;
		}

		return m_player.OpenSound();
	}

	public int CloseSound() {
		if (null == m_player) {
			return Constants.fail;
		}

		return m_player.CloseSound();
	}

	public int SetVolume(int nVolume) {
		if (null == m_player) {
			return Constants.fail;
		}

		return m_player.SetVolume(nVolume);
	}

	public int GetVolume() {
		if (null == m_player) {
			return Constants.fail;
		}

		return m_player.GetVolume();
	}

	// unit: kB/s
	public int GetCodeRate() {
		return m_codeRate;
	}

	public int StartVoiceTalk(AudioCaptureCallBack callBack) {
		if (null == m_player) {
			return Constants.fail;
		}

		return m_player.StartVoiceTalk(callBack);
	}

	public int StopVoiceTalk() {
		if (null == m_player) {
			return Constants.fail;
		}

		return m_player.StopVoiceTalk();
	}

	public int GetPlayTime() {
		long curTimestamp = System.currentTimeMillis();

		if (m_startTimestamp > 0) {
			return (int) ((curTimestamp - m_startTimestamp) / 1000);
		}
		return 0;
	}

	public long GetAllRecvLen() {
		return m_allRecvLen;
	}

	@Override
	public void fBackPlayDataCallBack(int playerId, int iDataType,
			byte[] pDataBuffer, int iDataSize) {
		// TODO Auto-generated method stub
		if (iFlag < 10) {
			Log.e("iDataType iDataSize", "" + iDataType + " " + iDataSize);
		}

		iFlag++;

		if (null == m_player) {
			notifyFailed();
			DebugLogger.i("StreamPlayer: null" + m_player);
			return;
		}

		// start play
		if (!m_player.IsPlaying()) {
			//
			DebugLogger.i("StreamPlayer: !IsPlaying" + m_player);
			if (Constants.DEVICE_TYPE_HK_DEV != m_deviceType
					&& Constants.DEVICE_TYPE_HK_PUSHDEV != m_deviceType) {
				if (Constants.StreamDataType_Head != iDataType
						&& Constants.success != m_player.StartPlay(null, 0,
								Constants.WMStreamType_File, m_showObj)) {
					notifyFailed();
					Log.e("clientsdk.jar", "start play fail, no head.");
					return;
				}
			} else {
				// open stream
				if (Constants.StreamDataType_Head == iDataType
						&& Constants.success != m_player.StartPlay(pDataBuffer,
								iDataSize, Constants.WMStreamType_File,
								m_showObj)) {
					notifyFailed();
					Log.e("clientsdk.jar", "start play fail, headsize:"
							+ iDataSize);
					return;
				}
			}
		} else {
			notifySuccess();
			
			if (Constants.StreamDataType_Head == iDataType && bHeadHasCome) {
				return;
			}
			
			m_player.InputData(pDataBuffer, iDataSize);
		}
		
		if (Constants.StreamDataType_Head == iDataType) {
			bHeadHasCome = true;
		}

		long curTimestamp = System.currentTimeMillis();
		if (m_lastTimestamp == 0) {
			m_lastTimestamp = curTimestamp;
			m_startTimestamp = curTimestamp;
		}

		// add size
		m_recvLen += iDataSize;
		m_allRecvLen += iDataSize;

		// code rate
		long interval = curTimestamp - m_lastTimestamp;
		if (interval >= StatisticInterval) {
			m_codeRate = (int) (m_recvLen / (interval / 1000) / 1024);

			// reset
			m_lastTimestamp = curTimestamp;
			m_recvLen = 0;
		}

	}

	// test
	public void setStartTime(long nTime) {
		m_startTime = nTime;
	}

	public void setHasDataCallBack(OnHasRealDataCallBackListener listener) {
		mHasRealDataCallBackListener = listener;
	}
}