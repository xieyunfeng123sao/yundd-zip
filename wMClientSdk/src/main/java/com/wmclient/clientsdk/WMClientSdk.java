/**
*Veye---����Ƶ������ƽӿ�API
*@author samLiang
*����ʱ�䣺2015-06-25
*/

package com.wmclient.clientsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;
import android.R.integer;
import android.R.string;
import android.util.Log;
import android.view.SurfaceHolder;


public class WMClientSdk 
{
	public static final int VeyeDevVoiceEncodeType_G711A = 0;
	public static final int VeyeDevVoiceEncodeType_AAC = 1;
	public static final int VeyeDevVoiceEncodeType_G711U = 2;
	
	private static WMClientSdk m_sdkinstance = new WMClientSdk();
	public static final String HOST = "http://www.vomont.com/veye/";
	
	public interface OnHasRealDataCallBackListener {
		public void OnHasRealDataCallBack();
	}
	
	public interface OnEncodeDataCallBack
	{
		boolean OnEncodeDataCallBack(byte[] data, int nSize, long nPts);
	}
	
	public interface DevStatusCallBack {
		
		/*状态回调*/
		public static final int DevStatus_Offline = 0;              //掉线
		public static final int DevStatus_Online = 1;				//上线
		
		/* 
		* nDevId 设备id, nChannelId 通道id
		*/
		
		public void OnDevStatusCallBack(int nDevId, int nStatus);
	}
	
	private Map m_streamPlayerMap = new HashMap();
	private Map m_vodPlayerMap = new HashMap();
	public Map m_playBackPlayerMap = new HashMap();
	private ClientEngineer m_engineer = new ClientEngineer();
	
	private WMDeviceGroup[] m_deviceGroupList = null;
	private WMDeviceInfo[] m_deviceList = null;
	private WMMapNodeInfo[] m_mapNodeList = null;
	private WMFileInfo[] m_fileList = null;
	private WMFileInfo[] m_frontEndfileList = null;
	
	private AudioCapturer mAudioCapturer = null;

	public static WMClientSdk getInstance() 
	{
        return m_sdkinstance;
    }

	/**
	*API对象初始化
	*@param logLevel 日志等级(默认为63，即0xff)
	*@return 0表示成功，其它表示失败原因
    */
	public int init(int logLevel) 
	{
		return m_engineer.init(logLevel);
	}
	
	/**
	 *API对象析构
    */	
	public void uninit() 
	{
		m_engineer.uninit();
	}
	
	/**
	 *API获取错误码
    */	
	public int getLastError() 
	{
		return m_engineer.GetLastError();
	}

	/**
	*用户登录
	*@param userName 用户名
	*@param password 密码
	*@param svrIp 服务器IP
	*@param svrPort 服务器端口
	*@return 0表示成功，其它表示失败原因
    */	
	public int login(String userName, String password, String svrIp, int svrPort) 
	{
		return m_engineer.login(userName, password, svrIp, svrPort);
	}
	
	/**
	 *当前用户退出
    */	
	public int logout()
	{
		return m_engineer.logout();
	}
	
	/**
	*当前用户修改密码
	*@param oldPassword 原始密码
	*@param newPassword 新密码
	*@return 0表示成功，其它表示失败原因
    */			
	public int updatePassword(String oldPassword, String newPassword)
	{
		return m_engineer.UpdatePassword(oldPassword, newPassword);
	}
	
	/**
	*获取设备组列表
	*@param deviceGroupList 设备组列表
	*@return 0表示成功，其它表示失败原因
    */	
	public int getDeviceGroupList(List<WMDeviceGroup> deviceGroupList) 
	{
		if(m_deviceGroupList == null)
		{
			m_deviceGroupList = m_engineer.GetDeviceGroupList();
		}	
		
		//add
		for(int i=0; i<m_deviceGroupList.length; i++)
		{
			deviceGroupList.add(m_deviceGroupList[i]);
		}
		
		return Constants.success;
	}	

	/**
	*获取设备列表
	*@param deviceList 设备列表
	*@param bForceRefresh 是否强制刷新获取新的设备列表
	*@return 0表示成功，其它表示失败原因
    */	
	public int getDeviceList(List<WMDeviceInfo> deviceList, boolean bForceRefresh) 
	{
		if(m_deviceList == null || bForceRefresh)
		{
			m_deviceList = m_engineer.GetDeviceList();
		}	
		
		//add
		for(int i=0; i<m_deviceList.length; i++)
		{
			deviceList.add(m_deviceList[i]);
		}
		
		return Constants.success;
	}
	
	/**
	*获取地图节点列表
	*@param nodeList 地图节点列表
	*@return 0表示成功，其它表示失败原因
    */	
	public int getMapNodeList(List<WMMapNodeInfo> nodeList) 
	{			
		if(m_mapNodeList == null)
		{
			m_mapNodeList = m_engineer.GetMapNodeList();
		}	
		
		//add
		for(int i=0; i<m_mapNodeList.length; i++)
		{
			nodeList.add(m_mapNodeList[i]);
		}
		
		return Constants.success;
	}
	
	/**
	*创建流媒体播放器
	*@param deviceType 设备类型（当前支持雄迈或者海康）
	*@param showObj 视频显示的窗口对象
	*@return 流媒体播放器对象
    */	
	public StreamPlayer CreatePlayer(int deviceType, Object showObj, int streamType)
	{
		StreamPlayer streamPlayer =  new StreamPlayer(deviceType);
		streamPlayer.setShowObj(showObj, streamType);
		
		return streamPlayer;
	}

	/**
	*销毁流媒体播放器
	*@param player 流媒体播放器对象
	*@return 0表示成功，其它表示失败原因
    */		
	public int DestroyPlayer(StreamPlayer player)
	{
		player.stopPlay();
		player.setShowObj(null,0);
		
		return Constants.success;
	}	
	
	/**
	*开始实时播放
	*@param deviceId 设备Id
	*@param devChannelId 通道Id
	*@param player 流媒体播放器对象
	*@return 播放流Id
    */		
	public int startRealPlay(int deviceId, int devChannelId, StreamPlayer player, int streamType) 
	{
		int deviceType = player.getDeviceType();		
		if(Constants.DEVICE_TYPE_XM_DEV != deviceType && Constants.DEVICE_TYPE_HK_DEV != deviceType
			&& Constants.DEVICE_TYPE_HK_PUSHDEV != deviceType
			&& Constants.DEVICE_TYPE_VEYE_DEV != deviceType
			&& Constants.DEVICE_TYPE_RTSP_DEV != deviceType
			&& Constants.DEVICE_TYPE_ONVIF_DEV != deviceType)
		{
			return Constants.WMPLAYERID_INVALID;
		}
		
        //start play
		int playerId = m_engineer.StartRealPlay(deviceId, devChannelId, streamType, player);		
		if(Constants.WMPLAYERID_INVALID != playerId) 
		{	
			m_streamPlayerMap.put(playerId, player);
			//m_engineer.SetRealPlayCallBackFunc(playerId, player);
			player.setStartTime(System.currentTimeMillis());
		}
		
		return playerId;
	}
	
	/**
	*停止实时播放
	*@param playerId 播放流Id
	*@return 0表示成功，其它表示失败原因
    */	
	public int stopRealPlay(int playerId) 
	{
//		int ret = m_engineer.ResetRealPlayCallBackFunc(playerId);		
//		if(Constants.success == ret)
//		{
			m_engineer.StopRealPlay(playerId);	
			m_streamPlayerMap.remove(playerId);
//		}
				
		return Constants.success;
	}
	
	/**
	*云台控制
	*@param deviceId 设备Id
	*@param devChannelId 通道Id
	*@param ptzCommand 云台控制命令
	*@param nStop 启动或停止	
	*@param nSpeed 速度
	*@return 0表示成功，其它表示失败原因
    */	
	public int ptzControl(int deviceId, int devChannelId, int ptzCommand, int nStop, int nSpeed)
	{
		return m_engineer.PTZControl(deviceId, devChannelId, ptzCommand, nStop, nSpeed);		
	}
	
	/**
	*开始本地存储
	*@param playerId 流Id
	*@param fileName 存储文件路径
	*@return 0表示成功，其它表示失败原因
    */	
	public int startRecord(int playerId, String fileName)	
	{
		return m_engineer.StartRecord(playerId, fileName);
	}

	/**
	*停止本地存储
	*@param playerId 流Id
	*@return 0表示成功，其它表示失败原因
    */			
	public int stopRecord(int playerId)	
	{
		return m_engineer.StopRecord(playerId);
	}	

	/**
	*视频截图
	*@param playerId 流Id
	*@param fileName 存储文件路径
	*@return 0表示成功，其它表示失败原因
    */	
	public int saveSnapshot(int playerId, String fileName)
	{
		StreamPlayer streamPlayer = (StreamPlayer)m_streamPlayerMap.get(playerId);
		if(null == streamPlayer)
		{
			return Constants.fail;
		}
			
		return streamPlayer.saveSnapshot(fileName);
	}
	
	/**
	*回放视频截图
	*@param playerId 流Id
	*@param fileName 存储文件路径
	*@return 0表示成功，其它表示失败原因
    */	
	public int savePlayBackSnapshot(int playerId, String fileName)
	{
		StreamPlayer streamPlayer = (StreamPlayer)m_playBackPlayerMap.get(playerId);
		if(null == streamPlayer)
		{
			return Constants.fail;
		}
			
		return streamPlayer.saveSnapshot(fileName);
	}
	
	/**
	*打开声音
	*@param playerId 流Id
	*@return 0表示成功，其它表示失败原因
    */			
	public int openSound(int playerId)
	{
		StreamPlayer streamPlayer = (StreamPlayer)m_streamPlayerMap.get(playerId);
		if(null == streamPlayer)
		{
			return Constants.fail;
		}
		
		return streamPlayer.OpenSound();
	}
	
	/**
	*关闭声音
	*@param playerId 流Id
	*@return 0表示成功，其它表示失败原因
    */			
	public int closeSound(int playerId)
	{
		StreamPlayer streamPlayer = (StreamPlayer)m_streamPlayerMap.get(playerId);
		if(null == streamPlayer)
		{
			return Constants.fail;
		}
		
		return streamPlayer.CloseSound();		
	}	
	
	/**
	*开始语音对讲
	*@param deviceId 设备Id
	*@param devChannelId 通道Id
	*@param playerId 流Id
	*@return 0表示成功，其它表示失败原因
    */		
	public int StartVoiceTalk(int deviceId, int devChannelId, int playerId, OnEncodeDataCallBack callBack)
	{
		StreamPlayer streamPlayer = (StreamPlayer)m_streamPlayerMap.get(playerId);
		if(null == streamPlayer)
		{
			return Constants.fail;
		}
		
		if (null != mAudioCapturer) {
			return Constants.fail;
		}
		
		if (Constants.success != m_engineer.StartVoiceTalk(deviceId, devChannelId, playerId))
		{
			return Constants.fail;
		}
		
		mAudioCapturer = new AudioCapturer();
		mAudioCapturer.startAudio(AudioCapturer.mSampleRate, AudioCapturer.mChannels, AudioCapturer.mAudiobitRate, callBack);
		
		return Constants.success;		
	}

	/**
	*停止语音对讲
	*@param deviceId 设备Id
	*@param devChannelId 通道Id
	*@param playerId 流Id
	*@return 0表示成功，其它表示失败原因
    */			
	public int StopVoiceTalk(int deviceId, int devChannelId, int playerId)
	{
		if (null != mAudioCapturer) {
			mAudioCapturer.stopAudio();
			mAudioCapturer = null;
		}
	
		StreamPlayer streamPlayer = (StreamPlayer)m_streamPlayerMap.get(playerId);
		if(null == streamPlayer)
		{
			return Constants.fail;
		}
		
		return m_engineer.StopVoiceTalk(deviceId, devChannelId, playerId);		
	}	
	
	/**
	*发送语音对讲
	*@param deviceId 设备Id
	*@param devChannelId 通道Id
	*@param dataBuf 数据Buf
	*@param dataLen 数据长度
	*@return 0表示成功，其它表示失败原因
    */	
	
	public int SendVoiceTalkData(int deviceId, int devChannelId, byte[] dataBuf, int dataLen)
	{
		return m_engineer.SendVoiceTalkData(deviceId, devChannelId, dataBuf, dataLen);
	}
	
	public int searchFileList(List<WMFileInfo> fileList, int deviceId, int devChannelId, long startTime, long endTime) 
	{
		m_fileList = m_engineer.FileSearch(deviceId, devChannelId, startTime, endTime);
		
		//add
		for(int i=0; i<m_fileList.length; i++)
		{
			fileList.add(m_fileList[i]);
		}
		
		return Constants.success;
	}
	
	public int startFilePlay(WMFileInfo info, StreamPlayer player) 
	{
		int deviceType = player.getDeviceType();		
		if(Constants.DEVICE_TYPE_XM_DEV != deviceType && Constants.DEVICE_TYPE_HK_DEV != deviceType
			&& Constants.DEVICE_TYPE_HK_PUSHDEV != deviceType)
		{
			return Constants.WMPLAYERID_INVALID;
		}
		
        //start play
		int playerId = m_engineer.StartFilePlay(info.getUrl(), info.getStartTime(), info.getEndTime(), player);		
		if(Constants.WMPLAYERID_INVALID != playerId) 
		{	
			m_vodPlayerMap.put(playerId, player);
			//m_engineer.SetRealPlayCallBackFunc(playerId, player);
		}
		
		return playerId;
	}
	
	public int stopFilePlay(int playerId) 
	{
//		int ret = m_engineer.ResetRealPlayCallBackFunc(playerId);		
//		if(Constants.success == ret)
//		{
			m_engineer.StopFilePlay(playerId);	
			m_vodPlayerMap.remove(playerId);
//		}
				
		return Constants.success;
	}
	
	public int getFilePlayPos(int playerId) 
	{
		return m_engineer.GetFilePlayPos(playerId);
	}
	
	public int setFilePlayPos(int playerId, int nPos) 
	{
		return m_engineer.SetFilePlayPos(playerId, nPos);
	}
	
	/**
	*设置接收到第一个数据包的回调
	*@param playerId 流Id
	*@param listener 回调监听事件
	*@return 0表示成功，其它表示失败原因
    */			
	public int setHasDataCallBack(StreamPlayer player, OnHasRealDataCallBackListener listener){
		if(null == player)
		{
			return Constants.fail;
		}
		
		player.setHasDataCallBack(listener);
		
		return Constants.success;
	}
	
	public int setDevLocationCB(DevLocationCallBack listener){
		return m_engineer.SetDevLocationCB(listener);
	}
	
	/**
	*设置报警回调
	*@param listener 回调监听事件
	*@return 0表示成功，其它表示失败原因
    */			
	public int setAlarmMessageCB(AlarmMessageCallBack listener){
		return m_engineer.SetAlarmMessageCB(listener);
	}
	
	/**
	*设置状态回调
	*@param listener 回调监听事件
	*@return 0表示成功，其它表示失败原因
    */			
	public int setDevStatusCB(DevStatusCallBack listener){
		return m_engineer.SetDevStatusCB(listener);
	}
	
	/**
	*开发者鉴权
	*@param userId 用户id
	*@param key 密钥
	*@return 0表示成功，其它表示失败原因
    */	
	@SuppressWarnings("deprecation")
	public int authenticate(String userId, String key, String serverAddress, int serverPort) 
	{
		return m_engineer.login(userId, key, serverAddress, serverPort);
	}
	
	/**
	 *释放鉴权
    */	
	public int unAuthenticate()
	{
		return m_engineer.logout();
	}
	
	/**
	*检索前端录像
	*@param fileList 搜索的文件列表结果
	*@param deviceId 设备id
	*@param devChannelId 通道id
	*@param startTime 开始时间戳
	*@param endTime 结束时间戳
	*@return 0表示成功，其它表示失败原因
    */	
	
	public int searchFrontEndFileList(List<WMFileInfo> fileList, int deviceId, int devChannelId, long startTime, long endTime) 
	{
		m_frontEndfileList = m_engineer.FrontEndFileSearch(deviceId, devChannelId, startTime, endTime);
		
		for(int i=0; i<m_frontEndfileList.length; i++)
		{
			fileList.add(m_frontEndfileList[i]);
		}
		
		return Constants.success;
	}
	
	
	/**
	*开始前端文件播放
	*@param nPos 播放进度  1-10000
	*@param info 文件信息
	*@param player 流媒体播放器对象
	*@return 播放流Id
    */	
	public int startFrontEndFilePlay(int nPos, WMFileInfo info, StreamPlayer player) 
	{
		int deviceType = player.getDeviceType();		
		if(Constants.DEVICE_TYPE_XM_DEV != deviceType && Constants.DEVICE_TYPE_HK_DEV != deviceType
			&& Constants.DEVICE_TYPE_HK_PUSHDEV != deviceType)
		{
			return Constants.WMPLAYERID_INVALID;
		}
		
        //start play
		int playerId = m_engineer.StartFrontEndFilePlay(info.getUrl(), nPos, info.getFileSize(), info.getStartTime(), info.getEndTime(), player);		
		if(Constants.WMPLAYERID_INVALID != playerId) 
		{	
			m_playBackPlayerMap.put(playerId, player);
		}
		
		return playerId;
	}
	
	/**
	*停止前端文件播放
	*@param playerId 播放流Id
	*@return 0表示成功，其它表示失败原因
    */	
	public int stopFrontEndFilePlay(int playerId) 
	{
		m_engineer.StopFrontEndFilePlay(playerId);	
		m_playBackPlayerMap.remove(playerId);
				
		return Constants.success;
	}
	
	/**
	*获取播放经度
	*@param playerId 播放流Id
	*@return 进度值 0-10000
    */	
	public int getFrontEndFilePlayPos(int playerId) 
	{		
		return m_engineer.GetFrontEndFilePlayPos(playerId);
	}
	
	public int AuthorizeDevice(int nGroupId, int nDevType, String szName, int nChannelCnt, String szSerialNumber)
	{
		return m_engineer.AuthorizeDevice(nGroupId, nDevType, szName, nChannelCnt, szSerialNumber);
	}
	    
	public String SearchAuthorizeDevice(String szSerialNumber)
	{
		return m_engineer.SearchAuthorizeDevice(szSerialNumber);
	}
}
