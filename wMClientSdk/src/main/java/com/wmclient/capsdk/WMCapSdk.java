package com.wmclient.capsdk;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.gson.Gson;

public class WMCapSdk
{
    private static WMCapSdk m_sdkinstance = new WMCapSdk();
    
    private Api m_api = new Api();
    
    private VideoCapturer mVideoCapturer = new VideoCapturer();
    
    private AudioCapturer mAudioCapturer = new AudioCapturer();
    
    private Context mContext = null;
    
    private String mImei = null;
    
    public ConfigInfo mConfigInfo = null;
    
    public Muxer mMuxer = null;
    
    public boolean mbUploadData = false;
    
    public boolean mbUploadVideoData = false;
    
    public boolean mbUploadAudioData = false;
    
    // public boolean mbStartVoiceTalk = false;
    
    private VoiceTalkPlayer mVoiceTalkPlayer = null;
    
    private SurfaceHolder mSurfaceHolder = null;
    
    private String TAG = "WMCapSdk";
    
    public interface OnEncodeDataCallBack
    {
        boolean OnEncodeDataCallBack(byte[] data, int nSize, long nPts);
    }
    
    public interface OnRemoteDefinitionListen
    {
        int OnRemoteDefinitionCB(int definitionType);
    }
    
    public interface OnVisitorNumChangeListen
    {
        int OnVisitorNumChangeCB(int nVisitorNum);
    }
    
    public static WMCapSdk getInstance()
    {
        return m_sdkinstance;
    }
    
    public int Init(Context context, int nLogLevel)
    {
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        mImei = "a_" + TelephonyMgr.getDeviceId();
        
        int nResult = m_api.Init(nLogLevel);
        if (Api.Api_Code_Result_OK != nResult)
        {
            return nResult;
        }
        
        m_api.SetStatusCB(this);
        m_api.SetRealPlayCB(this);
        m_api.SetStopRealPlayCB(this);
        
        m_api.SetStartVoiceTalkCB(this);
        m_api.SetStopVoiceTalkCB(this);
        m_api.SetVoiceTransDataCB(this);
        
        mContext = context;
        
        return nResult;
    }
    
    public String GetDeviceId()
    {
        // "VEYE-" +
        return "VEYE-" + mImei;
    }
    
    public String getID()
    {
        return mImei;
    }
    
    public int Uninit()
    {
        if (null != mVideoCapturer)
        {
            mVideoCapturer.stopAllRealPlay();
            mVideoCapturer.stopVideo();
        }
        
        if (null != mAudioCapturer)
        {
            mAudioCapturer.stopAllRealPlay();
            mAudioCapturer.stopAudio();
        }
        m_api.Stop();
        return m_api.Uninit();
    }
    
    public int SetConfigInfo(String mUserName, String mPassWord, String mSvrName, int mSvrType, String mSvrIP, int mSvrPort, int nWidth, int nHight, int nFrameRate, int nSampleRate, int nChannels,
        int nHasVideo, int nHasAudio)
    {
        mConfigInfo = new ConfigInfo(mImei, mUserName, mPassWord, mSvrName, mSvrType, mSvrIP, mSvrPort);
        mConfigInfo.mWidth = nWidth;
        mConfigInfo.mHight = nHight;
        mConfigInfo.mFrameRate = nFrameRate;
        mConfigInfo.mSampleRate = nSampleRate;
        mConfigInfo.mChannels = nChannels;
        mConfigInfo.mHasAudio = nHasAudio;
        mConfigInfo.mHasVideo = nHasVideo;
        
        Gson gson = new Gson();
        String jsonString = gson.toJson(mConfigInfo, ConfigInfo.class);
        
        return m_api.SetConfigInfo(jsonString);
    }
    
    public int ChangeVideoConfigInfo(int nWidth, int nHight, int nFrameRate, int nHasVideo)
    {
        mConfigInfo.mWidth = nWidth;
        mConfigInfo.mHight = nHight;
        mConfigInfo.mFrameRate = nFrameRate;
        mConfigInfo.mHasVideo = nHasVideo;
        return Api.Api_Code_Result_OK;
    }
    
    public int SetRemoteDefinitionListen(OnRemoteDefinitionListen callback)
    {
        return m_api.SetRemoteDefinitionCB(callback);
    }
    
    public int SetVisitorNumChangeListen(OnVisitorNumChangeListen callback)
    {
        return m_api.SetVisitorNumChangeCB(callback);
    }
    
    public int Start()
    {
        mbUploadData = false;
        mbUploadVideoData = true;
        mbUploadAudioData = true;
        
        int nResult = m_api.Start();
        if (Api.Api_Code_Result_OK != nResult)
        {
            return nResult;
        }
        
        return nResult;
    }
    
    public int Stop()
    {
        if (null != mVoiceTalkPlayer)
        {
            mVoiceTalkPlayer.stopPlay();
            mVoiceTalkPlayer = null;
        }
        
        return m_api.Stop();
    }
    
    public int StartVideoCapturer(SurfaceHolder holder, boolean bFront)
    {
        if (mConfigInfo.mHasVideo > 0)
        {
            mVideoCapturer.setSurfaceView(holder);
            if (!mVideoCapturer.startVideo(mConfigInfo.mWidth, mConfigInfo.mHight, mConfigInfo.mFrameRate, mConfigInfo.mBitRate, bFront, null))
            {
                mConfigInfo.mHasVideo = 0;
            }
        }
        
        return Api.Api_Code_Result_OK;
    }
    
    public int StopVideoCapturer()
    {
        if (null != mVideoCapturer)
        {
            mVideoCapturer.stopVideo();
        }
        
        return Api.Api_Code_Result_OK;
    }
    
    public int StartCapturer(SurfaceHolder holder, boolean bFront)
    {
        if (mConfigInfo.mHasVideo > 0)
        {
            mVideoCapturer.setSurfaceView(holder);
            if (!mVideoCapturer.startVideo(mConfigInfo.mWidth, mConfigInfo.mHight, mConfigInfo.mFrameRate, mConfigInfo.mBitRate, bFront, null))
            {
                mConfigInfo.mHasVideo = 0;
            }
        }
        
        //
        if (mConfigInfo.mHasAudio > 0)
        {
            if (!mAudioCapturer.startAudio(mConfigInfo.mSampleRate, mConfigInfo.mChannels, 0))
            {
                mConfigInfo.mHasAudio = 0;
            }
            
        }
        
        if (mConfigInfo.mHasVideo == 0 && mConfigInfo.mHasAudio == 0)
        {
            return Api.Api_Code_Result_Error;
        }
        
        return Api.Api_Code_Result_OK;
    }
    
    public int SetSurfaceHolder(SurfaceHolder holder)
    {
        mSurfaceHolder = holder;
        mVideoCapturer.setSurfaceView(holder);
        return Api.Api_Code_Result_OK;
    }
    
    public int StopCapturer()
    {
        if (null != mVideoCapturer)
        {
            mVideoCapturer.stopVideo();
        }
        
        if (null != mAudioCapturer)
        {
            mAudioCapturer.stopAudio();
        }
        
        return Api.Api_Code_Result_OK;
    }
    
    @SuppressWarnings("unused")
    public boolean OnLocationChangedCallBack(double longitude, double latitude, double altitude)
    {
        int m = 0;
        long nLongitude = (long)(longitude * Api.Api_Code_Gps_Prec);
        long nLatitude = (long)(latitude * Api.Api_Code_Gps_Prec);
        long nAltitude = (long)(altitude * Api.Api_Code_Gps_Prec);
        
        m = m_api.InputGps(System.currentTimeMillis() * 1000, nLongitude, nLatitude, nAltitude);
        return true;
    }
    
    public int InputData(int streamType, int nDataType, int nFrameType, byte[] dataBuf, int nBufSize, long nPts)
    {
        if (!mbUploadData)
        {
            return 0;
        }
        
        if (Api.Api_Code_DataType_H264 == nDataType && !mbUploadVideoData)
        {
            return 0;
        }
        
        if ((Api.Api_Code_DataType_AAC == nDataType || Api.Api_Code_DataType_Speex == nDataType) && !mbUploadAudioData)
        {
            return 0;
        }
        
        return m_api.InputData(streamType, nDataType, nFrameType, dataBuf, nBufSize, nPts);
    }
    
    public int setVideoUpload(boolean b)
    {
        mbUploadVideoData = b;
        return Api.Api_Code_Result_OK;
    }
    
    public int setAudioUpload(boolean b)
    {
        mbUploadAudioData = b;
        return Api.Api_Code_Result_OK;
    }
    
    public int OnStatusCB(int nSatus)
    {
        switch (nSatus)
        {
            case Api.Api_Code_Status_ConnError:
            case Api.Api_Code_Status_RigisterError:
            {
                mVideoCapturer.stopAllRealPlay();
                mAudioCapturer.stopAllRealPlay();
            }
                break;
            
            default:
                break;
        }
        return 0;
    }
    
    public RealPlayRet OnRealPlayCB(int nStreamType, int nMark)
    {
        RealPlayRet ret = new RealPlayRet();
        if (mConfigInfo.mHasVideo == 0 && mConfigInfo.mHasAudio == 0)
        {
            return null;
        }
        
        if (mConfigInfo.mHasVideo != 0)
        {
            if (Api.Api_Code_Result_OK != mVideoCapturer.startRealPlay(nStreamType, nMark))
            {
                return null;
            }
        }
        if (mConfigInfo.mHasAudio != 0)
        {
            if (Api.Api_Code_Result_OK != mAudioCapturer.startRealPlay(nStreamType, nMark))
            {
                mVideoCapturer.stopRealPlay(nStreamType, nMark);
                return null;
            }
        }
        
        ret.mResult = 0;
        
        ret.mHasVideo = mConfigInfo.mHasVideo;
        ret.mWidth = mConfigInfo.mWidth;
        ret.mHight = mConfigInfo.mHight;
        ret.mFrameRate = mConfigInfo.mFrameRate;
        ret.mBitRate = mConfigInfo.mBitRate;
        
        ret.mHasAudio = mConfigInfo.mHasAudio;
        ret.mSampleRate = mConfigInfo.mSampleRate;
        ret.mChannels = mConfigInfo.mChannels;
        
        mbUploadData = true;
        
        return ret;
    }
    
    public int OnStopRealPlayCB(int nStreamType, int nMark)
    {
        mVideoCapturer.stopRealPlay(nStreamType, nMark);
        mAudioCapturer.stopRealPlay(nStreamType, nMark);
        
        mbUploadData = false;
        
        return Api.Api_Code_Result_OK;
    }
    
    public int OnStartVoiceTalkCB()
    {
        Log.i(TAG, "OnStartVoiceTalkCB");
        
        if (null != mVoiceTalkPlayer)
        {
            Log.w(TAG, "OnStartVoiceTalkCB mVoiceTalkPlayer has");
            return Api.Api_Code_Result_OK;
        }
        
        mVoiceTalkPlayer = new VoiceTalkPlayer();
        mVoiceTalkPlayer.StartPlay(mSurfaceHolder);
        
        return Api.Api_Code_Result_OK;
    }
    
    public int OnStopVoiceTalkCB()
    {
        Log.i(TAG, "OnStopVoiceTalkCB");
        
        if (null == mVoiceTalkPlayer)
        {
            Log.w(TAG, "OnStopVoiceTalkCB mVoiceTalkPlayer not has");
            return Api.Api_Code_Result_OK;
        }
        
        mVoiceTalkPlayer.stopPlay();
        mVoiceTalkPlayer = null;
        
        return Api.Api_Code_Result_OK;
    }
    
    public int OnVoiceTransDataCB(int nEncodeType, int nSampleRate, int nChannel, byte[] data, int nSize)
    {
        Log.d(TAG, "OnVoiceTransDataCB");
        if (null == mVoiceTalkPlayer)
        {
            return -10023;
        }
        
        mVoiceTalkPlayer.InputData(nEncodeType, nSampleRate, nChannel, data, nSize);
        
        return Api.Api_Code_Result_OK;
    }
    
    public boolean IsNeedCodeVideoData()
    {
        if (null == mVideoCapturer)
        {
            return false;
        }
        
        return mVideoCapturer.IsNeedEncode();
    }
    
    @SuppressWarnings("deprecation")
    public boolean TakePicture(PictureCallback callBack)
    {
        if (null == mVideoCapturer)
        {
            return false;
        }
        
        return mVideoCapturer.TakePicture(callBack);
    }
    
    public boolean StartSaveFile(String path)
    {
        mMuxer = new Muxer();
        
        try
        {
            mMuxer.startMuxer(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean StopSaveFile()
    {
        if (null == mMuxer)
        {
            return false;
        }
        boolean ret = mMuxer.stopMuxer();
        mMuxer = null;
        return ret;
    }
    
    /*
     * 设置视频的远近
     */
    public void setZoom(int m)
    {
        mVideoCapturer.setZoom(m);
    }
    
    /*
     * 打开闪光灯
     */
    public void setOpenMode(boolean isOpen)
    {
        mVideoCapturer.openMode(isOpen);
    }
    
    public int getMaxZoom()
    {
        return mVideoCapturer.getMaxZoom();
    }
}
