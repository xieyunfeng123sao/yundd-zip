package com.wmclient.clientsdk;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wmclient.clientsdk.WMClientSdk.OnEncodeDataCallBack;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;

public class AudioEncoder {

	private static final String MIME_TYPE = "audio/mp4a-latm";
	private MediaCodec mMediaCodec = null;
	private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
	
	private int TIMEOUT_USEC = 12000;
	private long prevOutputPTSUs = 0;
	
	private OnEncodeDataCallBack mOnEncodeDataCallBack = null;
	
	private MediaFormat mOutformat = null;
	
	@SuppressLint("NewApi")
	public AudioEncoder(int SAMPLE_RATE, int channels, int BIT_RATE) throws IOException
	{   
		MediaCodecInfo audioCodecInfo = chooseAudioEncoder(MIME_TYPE);//是不是似曾相识?没错,一样是通过MIME拿到系统对应的编码器信息
		final MediaFormat audioFormat = MediaFormat.createAudioFormat(MIME_TYPE, SAMPLE_RATE, AudioCapturer.mChannelConfig);
		audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
		audioFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioCapturer.mChannelConfig);//CHANNEL_IN_STEREO 立体声
		audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
		audioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channels);
		audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 16384);
//		      audioFormat.setLong(MediaFormat.KEY_MAX_INPUT_SIZE, inputFile.length());
//		      audioFormat.setLong(MediaFormat.KEY_DURATION, (long)durationInMs );
		mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);

		mMediaCodec.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
		mMediaCodec.start();
    } 
	
    @SuppressLint("NewApi")  
    public void close() {  
        try {  
        	mMediaCodec.stop();  
        	mMediaCodec.release();  
        } catch (Exception e){   
            e.printStackTrace();  
        }  
    }  
	
	public void setEncodeDataCallBack(OnEncodeDataCallBack callBack)
	{
		mOnEncodeDataCallBack = callBack;
	}

	// choose the audio encoder by name.
    @SuppressLint("NewApi")
	private MediaCodecInfo chooseAudioEncoder(String name) {
     int nbCodecs = MediaCodecList.getCodecCount();
     for (int i = 0; i < nbCodecs; i++) {
         MediaCodecInfo mci = MediaCodecList.getCodecInfoAt(i);
         if (!mci.isEncoder()) {
             continue;
         }
         String[] types = mci.getSupportedTypes();
         for (int j = 0; j < types.length; j++) {
             if (types[j].equalsIgnoreCase(name)) {
                 //Log.i(TAG, String.format("vencoder %s types: %s", mci.getName(), types[j]));
                 if (name == null) {
                     return mci;
                 }

                 if (mci.getName().contains(name)) {
                     return mci;
                 }
             }
         }
     }
     
     return null;
 }
    
    public long getPTSUs()
    {
    	return System.currentTimeMillis();
    }
    
    @SuppressLint("NewApi")
    public void encode(byte[] buffer, int length, long presentationTimeUs) {
    	final ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
    	final int inputBufferIndex = mMediaCodec.dequeueInputBuffer(-1);
    	 /*向编码器输入数据*/
    	if (inputBufferIndex >= 0)
    	{
    		final ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
    		inputBuffer.clear();
    		if (buffer != null)
    		{
    			inputBuffer.put(buffer);
    			mMediaCodec.queueInputBuffer(inputBufferIndex, 0, length,
    					presentationTimeUs, 0);
    			
    		}
    		else 
    		{
    			mMediaCodec.queueInputBuffer(inputBufferIndex, 0, 0,
    					presentationTimeUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
    		}
    	} 
    	else if (inputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER)
    	{
    	}
    	 //上面的过程和视频是一样的,都是向输入缓冲区输入原始数据

    	 /*获取解码后的数据*/
    	ByteBuffer[] encoderOutputBuffers = mMediaCodec.getOutputBuffers();
    	int encoderStatus;
    	do {
    		encoderStatus = mMediaCodec.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
    		if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER)
    		{
    		} 
    		else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED)
    		{
    			encoderOutputBuffers = mMediaCodec.getOutputBuffers();
    		} 
    		else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED)
    		{
    			mOutformat = mMediaCodec.getOutputFormat(); // API >= 16
    		}
    		else if (encoderStatus < 0)
    		{
    		}
    		else
    		{
    			final ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
    			if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0)
    			{
    				mBufferInfo.size = 0;
    			}

    			if (mBufferInfo.size != 0)
    			{
    				mBufferInfo.presentationTimeUs = System.currentTimeMillis()*1000;
    				
    				//call back
    				if (null != mOnEncodeDataCallBack) {
    					byte[] outData = new byte[mBufferInfo.size + 12];  
    					int nOffSet = 0;
    					nOffSet = CommonUtil.writeToStream(outData, nOffSet, WMClientSdk.VeyeDevVoiceEncodeType_AAC);
    					nOffSet = CommonUtil.writeToStream(outData, nOffSet, AudioCapturer.mSampleRate);
    					nOffSet = CommonUtil.writeToStream(outData, nOffSet, AudioCapturer.mChannels);
    					
    					encodedData.get(outData, nOffSet, mBufferInfo.size); 
    		                
    					long nCurrentTime = System.currentTimeMillis();
    					mOnEncodeDataCallBack.OnEncodeDataCallBack(outData, outData.length, nCurrentTime*1000);
					}

    				prevOutputPTSUs = mBufferInfo.presentationTimeUs;
    			}

    			mMediaCodec.releaseOutputBuffer(encoderStatus, false);
    		}
    	} while (encoderStatus >= 0);
    }
}
