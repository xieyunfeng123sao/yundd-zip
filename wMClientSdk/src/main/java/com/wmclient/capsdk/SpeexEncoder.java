package com.wmclient.capsdk;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.code.CommonUtil;
import com.code.Speex;
import com.wmclient.capsdk.WMCapSdk.OnEncodeDataCallBack;

import android.util.Log;

public class SpeexEncoder implements Runnable
{
    private String TAG = "SpeexEncoder";
    
    private int mEncoderId = Speex.CoderId_Err;
    
    private boolean mbProcess = false;
    
    private final byte[] lockRecord = new byte[0];
    
    private final byte[] lockTrack = new byte[0];
    
    private List<TrackData> listTrack = null;
    
    private List<RecordData> listRecord = null;
    
    public static int encoder_packagesize = 1024;
    
    public static int encoder_packagenum = AudioCapturer.mBufferSizeInBytes / 160;
    
    private byte[] processedData = new byte[4 + encoder_packagenum * (4 + encoder_packagesize)];
    
    private volatile boolean isRecording;
    
    private OnEncodeDataCallBack mOnEncodeDataCallBack = null;
    
    public SpeexEncoder()
    {
        listRecord = Collections.synchronizedList(new LinkedList<RecordData>());
        listTrack = Collections.synchronizedList(new LinkedList<TrackData>());
    }
    
    public boolean startEncoder(int nSampleRate, int nChannels, int bitrate)
    {
        synchronized (lockRecord)
        {
            int nRet = Speex.getInstance().encoderOpen(Speex.DEFAULT_COMPRESSION, nSampleRate, 160 * 15);
            if (Speex.CoderId_Err == nRet)
            {
                Log.e(TAG, "encoderOpen fail");
                
                return false;
            }
            
            mEncoderId = nRet;
        }
        
        Log.i(TAG, "startEncoder success");
        
        return true;
    }
    
    public void stopEncoder()
    {
        synchronized (lockRecord)
        {
            Speex.getInstance().encoderClose(mEncoderId);
            mEncoderId = Speex.CoderId_Err;
        }
    }
    
    public void setEncodeDataCallBack(OnEncodeDataCallBack callBack)
    {
        synchronized (lockRecord)
        {
            mOnEncodeDataCallBack = callBack;
        }
    }
    
    public int getProcessedSize()
    {
        synchronized (lockRecord)
        {
            return Speex.getInstance().getEncoderFrameSize(mEncoderId);
        }
    }
    
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        // 启动编码线程
        
        int nPkgNum = 0;
        int nDataLen = 4;
        
        int nCount = 0;
        long nPts = 0;
        while (isRecording())
        {
            synchronized (lockRecord)
            {
                if (listRecord.size() == 0)
                {
                    try
                    {
                        lockRecord.wait();
                    }
                    catch (InterruptedException e)
                    {
                        throw new IllegalStateException("Wait() interrupted!", e);
                    }
                }
            }
            
            if (listRecord.size() > 0)
            {
                synchronized (lockRecord)
                {
                    RecordData record = listRecord.remove(0);
                    int getSize = 0;
                    
                    if (listTrack.size() != 0)
                    {
                        TrackData track = listTrack.remove(0);
                        
                        getSize = Speex.getInstance().encodeData(mEncoderId, record.data, 0, record.size, track.data, 0, track.size, processedData, nDataLen + 4, false, false);
                    }
                    else
                    {
                        getSize = Speex.getInstance().encodeData(mEncoderId, record.data, 0, record.size, null, 0, 0, processedData, nDataLen + 4, false, false);
                    }
                    
                    nCount++;
                    nPts += record.pts;
                    
                    if (getSize > 0)
                    {
                        CommonUtil.memToStream(processedData, nDataLen, getSize);
                        nDataLen += 4 + getSize;
                        nPkgNum += 1;
                    }
                    
                    if (nCount >= encoder_packagenum)
                    {
                        CommonUtil.memToStream(processedData, 0, nPkgNum);
                        
                        // send data
                        if (null != mOnEncodeDataCallBack)
                        {
                            mOnEncodeDataCallBack.OnEncodeDataCallBack(processedData, nDataLen, nPts / nCount);
                        }
                        
                        nPkgNum = 0;
                        nDataLen = 4;
                        
                        nCount = 0;
                        nPts = 0;
                    }
                }
            }
            
        }
        
        return;
    }
    
    public void putRecordData(short[] rData, int size, long pts)
    {
        synchronized (lockRecord)
        {
            if (!isRecording)
            {
                return;
            }
            
            if (listRecord.size() > encoder_packagenum)
            {
                System.out.println("record data num is enough");
                return;
            }
            
            RecordData recordData = new RecordData();
            recordData.size = size;
            recordData.pts = pts;
            System.arraycopy(rData, 0, recordData.data, 0, size);
            listRecord.add(recordData);
            lockRecord.notify();
        }
    }
    
    public void putTrackData(short[] tData, int size, long pts)
    {
        synchronized (lockRecord)
        {
            if (listTrack.size() > encoder_packagenum)
            {
                System.out.println("track data num is enough");
                
                return;
            }
            
            TrackData trackData = new TrackData();
            trackData.size = size;
            trackData.pts = pts;
            System.arraycopy(tData, 0, trackData.data, 0, size);
            listTrack.add(trackData);
            lockRecord.notify();
        }
    }
    
    public class RecordData
    {
        int size;
        
        long pts;
        
        private short[] data = new short[encoder_packagesize];
    }
    
    public class TrackData
    {
        int size;
        
        long pts;
        
        private short[] data = new short[encoder_packagesize];
    }
    
    public void setRecording(boolean isRecording)
    {
        synchronized (lockRecord)
        {
            this.isRecording = isRecording;
            lockRecord.notify();
        }
    }
    
    public boolean isRecording()
    {
        synchronized (lockRecord)
        {
            return isRecording;
        }
    }
}