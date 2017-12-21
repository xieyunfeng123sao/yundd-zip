package com.wmclient.capsdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

import com.code.CommonUtil;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

@SuppressLint("NewApi")
public class AvcEncoder
{
    public byte[] m_info = null;
    
    private Camera camera;
    
    private MediaCodec vencoder;
    
    private MediaCodecInfo vmci;
    
    private MediaCodec.BufferInfo vebi;
    
    private byte[] vbuffer;
    
    // video camera settings.
    private int vcolor;
    
    private int vbitrate_kbps = 300 * 1024;
    
    private static int VFPS = 10;
    
    private static int VGOP = 2;
    
    private static int VWIDTH = 640;
    
    private static int VHEIGHT = 480;
    
    private String VCODEC = "video/avc";
    
    private String TAG = "AvcEncoder";
    
    private int TIMEOUT_USEC = 12000;
    
    private boolean bKeyCome = false;
    
    private MediaFormat mOutformat = null;
    
    // public FileOutputStream mFile = null;
    
    public AvcEncoder(int width, int height, int framerate, int bitrate)
        throws IOException
    {
        
        VWIDTH = width;
        VHEIGHT = height;
        VFPS = framerate;
        vbitrate_kbps = width * height * 3;
        bKeyCome = false;
        
        // choose the right vencoder, perfer qcom then google.
        vcolor = chooseVideoEncoder();
        vencoder = MediaCodec.createByCodecName(vmci.getName());
        vebi = new MediaCodec.BufferInfo();
        // setup the vencoder.
        // @see https://developer.android.com/reference/android/media/MediaCodec.html
        MediaFormat vformat = MediaFormat.createVideoFormat(VCODEC, VWIDTH, VHEIGHT);
        vformat.setInteger(MediaFormat.KEY_COLOR_FORMAT, vcolor);
        vformat.setInteger(MediaFormat.KEY_BIT_RATE, 2 * width * height);
        vformat.setInteger(MediaFormat.KEY_FRAME_RATE, VFPS);
        vformat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, VGOP);
        
        // vformat.setInteger(MediaFormat.KEY_BIT_RATE, vbitrate_kbps);
        // vformat.setInteger(MediaFormat.KEY_FRAME_RATE, VFPS);
        // vformat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, VGOP);
        // the following error can be ignored:
        // 1. the storeMetaDataInBuffers error:
        // [OMX.qcom.video.encoder.avc] storeMetaDataInBuffers (output) failed w/ err -2147483648
        // @see http://bigflake.com/mediacodec/#q12
        vencoder.configure(vformat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        vencoder.start();
        
        //
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = path + File.separator + sDateFormat.format(new java.util.Date()) + ".mp4";
        
        // mFile = new FileOutputStream(fileName);
    }
    
    // for the vbuffer for YV12(android YUV), @see below:
    // https://developer.android.com/reference/android/hardware/Camera.Parameters.html#setPreviewFormat(int)
    // https://developer.android.com/reference/android/graphics/ImageFormat.html#YV12
    private int getYuvBuffer(int width, int height)
    {
        // stride = ALIGN(width, 16)
        int stride = (int)Math.ceil(width / 16.0) * 16;
        // y_size = stride * height
        int y_size = stride * height;
        // c_stride = ALIGN(stride/2, 16)
        int c_stride = (int)Math.ceil(width / 32.0) * 16;
        // c_size = c_stride * height/2
        int c_size = c_stride * height / 2;
        // size = y_size + c_size * 2
        return y_size + c_size * 2;
    }
    
    // choose the video encoder by name.
    private MediaCodecInfo chooseVideoEncoder(String name, MediaCodecInfo def)
    {
        int nbCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < nbCodecs; i++)
        {
            MediaCodecInfo mci = MediaCodecList.getCodecInfoAt(i);
            if (!mci.isEncoder())
            {
                continue;
            }
            String[] types = mci.getSupportedTypes();
            for (int j = 0; j < types.length; j++)
            {
                if (types[j].equalsIgnoreCase(VCODEC))
                {
                    // Log.i(TAG, String.format("vencoder %s types: %s", mci.getName(), types[j]));
                    if (name == null)
                    {
                        return mci;
                    }
                    
                    if (mci.getName().contains(name))
                    {
                        return mci;
                    }
                }
            }
        }
        return def;
    }
    
    // choose the right supported color format. @see below:
    // https://developer.android.com/reference/android/media/MediaCodecInfo.html
    // https://developer.android.com/reference/android/media/MediaCodecInfo.CodecCapabilities.html
    private int chooseVideoEncoder()
    {
        // choose the encoder "video/avc":
        // 1. select one when type matched.
        // 2. perfer google avc.
        // 3. perfer qcom avc.
        vmci = chooseVideoEncoder(null, null);
        // vmci = chooseVideoEncoder("google", vmci);
        // vmci = chooseVideoEncoder("qcom", vmci);
        
        int matchedColorFormat = 0;
        MediaCodecInfo.CodecCapabilities cc = vmci.getCapabilitiesForType(VCODEC);
        for (int i = 0; i < cc.colorFormats.length; i++)
        {
            int cf = cc.colorFormats[i];
            
            // choose YUV for h.264, prefer the bigger one.
            // corresponding to the color space transform in onPreviewFrame
            if ((cf >= cc.COLOR_FormatYUV420Planar && cf <= cc.COLOR_FormatYUV420SemiPlanar))
            {
                if (cf > matchedColorFormat)
                {
                    matchedColorFormat = cf;
                }
            }
        }
        for (int i = 0; i < cc.profileLevels.length; i++)
        {
            MediaCodecInfo.CodecProfileLevel pl = cc.profileLevels[i];
        }
        
        return matchedColorFormat;
    }
    
    @SuppressLint("NewApi")
    public void close()
    {
        try
        {
            vencoder.stop();
            vencoder.release();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public int getClor()
    {
        return vcolor;
    }
    
    public int input(byte[] input, long pts)
    {
        // try{
        // mFile.write(input);
        // }
        // catch(Exception e){
        // e.printStackTrace();
        // }
        
        ByteBuffer[] inputBuffers = vencoder.getInputBuffers();
        int inputBufferIndex = vencoder.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0)
        {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(input);
            vencoder.queueInputBuffer(inputBufferIndex, 0, input.length, pts, 0);
        }
        
        return 0;
    }
    
    public int offerEncoder(byte[] output, int nStreamType)
    {
        try
        {
            ByteBuffer[] outputBuffers = vencoder.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = vencoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
            if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED)
            {
                mOutformat = vencoder.getOutputFormat(); // API >= 16
                
                // 特别注意此处, 此处和视频编码是一样的
                Muxer mediaMuxer = WMCapSdk.getInstance().mMuxer;
                if (mediaMuxer != null && !mediaMuxer.isVideoAdd)
                {
                    // 添加音轨,和添加视轨都是一样的调用
                    mediaMuxer.addTrackIndex(Muxer.TRACK_VIDEO, mOutformat);
                }
            }
            
            while (outputBufferIndex >= 0)
            {
                long nCurrentTime = System.currentTimeMillis();
                
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                
                // Log.d(TAG, "data come: " + ", flags:"+bufferInfo.flags + ", szie:"+bufferInfo.size);
                
                int pos = 0;
                if (m_info != null && (bufferInfo.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) == 1)
                {
                    System.arraycopy(m_info, 0, output, 0, m_info.length);
                    pos += m_info.length;
                    
                    System.arraycopy(outData, 0, output, m_info.length, outData.length);
                    pos += outData.length;
                    
                    WMCapSdk.getInstance().InputData(nStreamType, Api.Api_Code_DataType_H264, Api.Api_Code_FrameType_I, output, pos, nCurrentTime * 1000);
                    
                    bKeyCome = true;
                    
                    Log.d(TAG, "IDR come: " + ", szie:" + outData.length);
                    
                    Muxer mediaMuxer = WMCapSdk.getInstance().mMuxer;
                    if (mediaMuxer != null)
                    {
                        if (!mediaMuxer.isVideoAdd && null != mOutformat)
                        {
                            mediaMuxer.addTrackIndex(Muxer.TRACK_VIDEO, mOutformat);
                        }
                        
                        // 添加音轨,和添加视轨都是一样的调用
                        bufferInfo.presentationTimeUs = nCurrentTime * 1000;
                        WMCapSdk.getInstance().mMuxer.inPutData(new MuxerData(Muxer.TRACK_VIDEO, outputBuffer, bufferInfo));
                    }
                }
                else if (bKeyCome)
                {
                    System.arraycopy(outData, 0, output, 0, outData.length);
                    pos += outData.length;
                    
                    WMCapSdk.getInstance().InputData(nStreamType, Api.Api_Code_DataType_H264, Api.Api_Code_FrameType_Other, output, pos, nCurrentTime * 1000);
                    Log.d(TAG, "other frame" + ", szie:" + outData.length);
                    
                    Muxer mediaMuxer = WMCapSdk.getInstance().mMuxer;
                    if (mediaMuxer != null)
                    {
                        if (!mediaMuxer.isVideoAdd && null != mOutformat)
                        {
                            // 特别注意此处, 此处和视频编码是一样的
                            mediaMuxer.addTrackIndex(Muxer.TRACK_VIDEO, mOutformat);
                        }
                        
                        // 添加音轨,和添加视轨都是一样的调用
                        bufferInfo.presentationTimeUs = nCurrentTime * 1000;
                        WMCapSdk.getInstance().mMuxer.inPutData(new MuxerData(Muxer.TRACK_VIDEO, outputBuffer, bufferInfo));
                    }
                }
                else if (bufferInfo.flags == 2)// 保存pps sps 只有开始时 第一个帧里有， 保存起来后面用
                {
                    ByteBuffer spsPpsBuffer = ByteBuffer.wrap(outData);
                    if (spsPpsBuffer.getInt() == 0x00000001)
                    {
                        m_info = new byte[outData.length];
                        System.arraycopy(outData, 0, m_info, 0, outData.length);
                    }
                    else
                    {
                        Log.e(TAG, "not get sps pps");
                        return -1;
                    }
                }
                else
                {
                    Log.e(TAG, "bufferInfo flags error, flags:" + bufferInfo.flags);
                }
                
                vencoder.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = vencoder.dequeueOutputBuffer(bufferInfo, 0);
            }
            
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        
        return 0;
    }
}