package com.wmclient.capsdk;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class VideoCapturer implements Camera.PreviewCallback, SurfaceHolder.Callback
{
    public static final int mFrameRate = 8;
    
    public static final int mMaxFrameRate = 60;
    
    // 640 480
    public static final int mDisWidth = 240;
    
    public static final int mDisHight = 320;
    
    public static final int mCapWidth = mDisHight;
    
    public static final int mCapHight = mDisWidth;
    
    public static final int mBitRate = 300 * 1024;
    
    private int nDiffTime = 1000 / mFrameRate; // ms
    
    private int nOffTime = 1000 / mMaxFrameRate;
    
    //
    private Camera mCamera = null;
    
    private SurfaceHolder mSurfaceHolder = null;
    
    private boolean mCameraPreview = false;
    
    private VideoEncoder mVideoEncoder = new VideoEncoder();
    
    private String TAG = "VideoCapturer";
    
    private boolean mbFront = false;
    
    private volatile boolean mbNeedCode = true;
    
    private volatile int mStreamType = 0;
    
    private int mWidth = mDisWidth;
    
    private int mHight = mDisHight;
    
    private int m_nFrameRate = mFrameRate;
    
    private boolean mbStartVideo = false;
    
    static public boolean mbLandscape = false;
    
    public VideoCapturer()
    {
        
    }
    
    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        // TODO Auto-generated method stub
        Log.i(TAG, "input decodeData: ");
        if (null == data)
        {
            return;
        }
        
        if (!mbNeedCode)
        {
            return;
        }
        
        Camera.Parameters p = mCamera.getParameters();
        
        mVideoEncoder.decodeData(data, data.length, p.getPreviewSize().width, p.getPreviewSize().height, mStreamType, mbFront);
        
    }
    
    public void setSurfaceView(SurfaceHolder holder)
    {
        if (null == holder)
        {
            return;
        }
        
        holder.addCallback(this);
        mSurfaceHolder = holder;
    }
    
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public boolean startVideo(int nWidth, int nHight, int nFrameRate, int nBitRate, boolean bFront, Handler handler)
    {
        //
        if (!mVideoEncoder.isStart())
        {
            mVideoEncoder.startEncoder(nWidth, nHight, nFrameRate, nBitRate);
        }
        
        int nCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        if (bFront)
        {
            nCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        else
        {
            nCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        
        //
        
        try
        {
            if (mCamera == null)
            {
                mCamera = Camera.open(nCameraFacing);
            }
            
            if (mCamera == null)
            {
                mVideoEncoder.stopEncoder();
                return false;
            }
            
            Camera.Parameters p = mCamera.getParameters();
            
            List<Size> mlist = p.getSupportedPreviewSizes();
         
            p.setPreviewSize(nHight, nWidth);
            p.setPreviewFormat(ImageFormat.NV21);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.setPreviewCallback(this);
            mCamera.setParameters(p);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        }
        catch (Exception e)
        {
            mVideoEncoder.stopEncoder();
            return false;
        }
        
        mCameraPreview = true;
        mbFront = bFront;
        mWidth = nWidth;
        mHight = nHight;
        m_nFrameRate = nFrameRate;
        mbStartVideo = true;
        
        return true;
    }
    
    /**
     * 
     * 视频镜头的伸缩
     * 改变zoom值 改变视频的伸缩距离
     * 
     * @param m 传入的值 不同手机支持的最大m不同
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public void setZoom(int m)
    {
        Camera.Parameters p = mCamera.getParameters();
        if (m <= 0)
        {
            p.setZoom(0);
        }
        else if (m >= p.getMaxZoom())
        {
            p.setZoom(p.getMaxZoom());
        }
        else
        {
            p.setZoom(m);
        }
        mCamera.setParameters(p);
    }
    
    public int getMaxZoom()
    {
        if (mCamera != null)
        {
            Camera.Parameters p = mCamera.getParameters();
            
            return p.getMaxZoom();
        }
        return 0;
    }
    
    /**
     * 
     * 
     * 打开闪光灯的接口
     * 
     * @param isOpen
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("NewApi")
    public void openMode(boolean isOpen)
    {
        Camera.Parameters p = mCamera.getParameters();
        if (isOpen)
        {
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            
        }
        else
        {
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
        }
        mCamera.setParameters(p);
    }
    
    @SuppressLint("NewApi")
    public boolean isSupportZoom()
    {
        boolean isSuppport = true;
        if (mCamera.getParameters().isSmoothZoomSupported())
        {
            isSuppport = false;
        }
        return isSuppport;
    }
    
    public void stopVideo()
    {
        if (mCamera != null)
        {
            if (mCameraPreview)
            {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCameraPreview = false;
            }
            mCamera.release();
            mCamera = null;
        }
        
        mVideoEncoder.stopEncoder();
        
        mbStartVideo = false;
    }
    
    public boolean IsNeedEncode()
    {
        return mbNeedCode;
    }
    
    public int startRealPlay(int nStreamType, int nMark)
    {
        mStreamType = nStreamType;
        return Api.Api_Code_Result_OK;
    }
    
    public int stopRealPlay(int nStreamType, int nMark)
    {
        return Api.Api_Code_Result_OK;
    }
    
    public void stopAllRealPlay()
    {
        
    }
    
    @SuppressWarnings("deprecation")
    public boolean TakePicture(PictureCallback callBack)
    {
        if (null == mCamera)
        {
            return false;
        }
        mCamera.takePicture(null, null, callBack);
        
        return true;
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        if (mbStartVideo && null == mCamera)
        {
            Log.d("holder", holder.toString());
            startVideo(mWidth, mHight, m_nFrameRate, VideoCapturer.mBitRate, mbFront, null);
        }
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        if (mCamera != null)
        {
            if (mCameraPreview)
            {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCameraPreview = false;
            }
            mCamera.release();
            mCamera = null;
        }
        
        mVideoEncoder.stopEncoder();
    }
}
