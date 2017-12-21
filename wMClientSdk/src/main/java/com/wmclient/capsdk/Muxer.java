package com.wmclient.capsdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

public class Muxer extends Thread{
	public static final int TRACK_VIDEO = 0;
	public static final int TRACK_AUDIO = 1;
	
	private String TAG = "Muxer";
	
	private Lock mMuxerDatalock = new ReentrantLock();
	private Lock mMuxerlock = new ReentrantLock();
	private Vector<MuxerData> muxerDatas = new Vector<MuxerData>();//缓冲传输过来的数据
	private MediaMuxer mediaMuxer = null;
	private boolean isExit = true;
	public boolean isVideoAdd = false;
	public boolean isAudioAdd = false;
	private boolean isMuxerStart = false;
	private int videoTrackIndex = -1;
	private int audioTrackIndex = -1;
	private boolean mbHasWriteIFrame = false;
	//private Thread mThread = null;
	
	@SuppressLint("NewApi")
	public void startMuxer(String filePath) throws IOException {

			Log.i(TAG, "start b");
			mMuxerlock.lock();
			isVideoAdd = false;
			isAudioAdd = false;
			mbHasWriteIFrame = false;

			//创建混合器

			mediaMuxer = new MediaMuxer(filePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
			mMuxerlock.unlock();
			


			//mThread = new Thread(this);
			this.start();

			isExit = false;
			
			Log.i(TAG, "start e");
	}
	
	public boolean isMuxerStart()
	{
		return isMuxerStart;
	}
	
	public boolean IsPre(){
		if ((WMCapSdk.getInstance().mConfigInfo.mHasVideo > 0)&&!isVideoAdd) {
			return false;
		}

		if ((WMCapSdk.getInstance().mConfigInfo.mHasAudio > 0)&&!isAudioAdd) {
			return false;
		}

		return true;
	}

	//混合器,最重要的就是保证再添加数据之前,要先添加视轨和音轨,并且保存响应轨迹的索引,用于添加数据的时候使用
	@SuppressLint("NewApi")
	public void addTrackIndex(int index, MediaFormat mediaFormat) {
			if (isMuxerStart()) {
				return;
			}
			
			if (isExit) {
				return;
			}

			Log.i(TAG, "addTrackIndex b");
			mMuxerlock.lock();
			if (null != mediaMuxer) {
				int track = mediaMuxer.addTrack(mediaFormat);
				
				if (index == TRACK_VIDEO) {
					videoTrackIndex = track;
					isVideoAdd = true;
					
					Log.i(TAG, "添加视轨");
				} 
				else 
				{
					audioTrackIndex = track;
					isAudioAdd = true;

					Log.i(TAG, "添加音轨");
				}
			}

			if (IsPre() && !isMuxerStart()) {
				requestStart();
			}

			synchronized (mMuxerDatalock) 
			{
				mMuxerDatalock.notify();
			}
			
			mMuxerlock.unlock();
			
			Log.i(TAG, "addTrackIndex e");
	}
	
	@SuppressLint("NewApi")
	public boolean stopMuxer()
	{
		Log.i(TAG, "stop b");

		//this.stop();
		int waitTime = 0;
		while (!mbHasWriteIFrame) {
			if (waitTime > 3000) {
				isExit = true;

				synchronized (mMuxerDatalock) 
				{
					mMuxerDatalock.notify();
				}

				return false;
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			waitTime += 100;
		}


		isExit = true;
		synchronized (mMuxerDatalock) 
		{
			mMuxerDatalock.notify();
		}

		mMuxerlock.lock();
		if (isMuxerStart()) {
			mediaMuxer.stop();
			isMuxerStart = false;
		}

		mediaMuxer.release();
		mediaMuxer = null;

		mMuxerlock.unlock();

		Log.i(TAG, "stop e");

		return true;
	}
	
	public void inPutData(MuxerData data)
	{
		if (!isMuxerStart) {
			return;
		}
		
		muxerDatas.add(data);
		
		synchronized (mMuxerDatalock) 
		{
			mMuxerDatalock.notify();
		}
	}

	@SuppressLint("NewApi")
	private void requestStart() {
		if (!isMuxerStart()) {
			if (null != mediaMuxer) {
				Log.i(TAG, "mediaMuxer  start b");
				mediaMuxer.start();//在start之前,确保视轨和音轨已经添加了
				isMuxerStart = true;
				Log.i(TAG, "mediaMuxer  start e");
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isExit) {
			if (muxerDatas.isEmpty())
			{
				synchronized (mMuxerDatalock) {
					try {
						mMuxerDatalock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} 
			else 
			{
				if (isMuxerStart()) 
				{
					MuxerData data = muxerDatas.remove(0);
					if (null == data) {
						continue;
					}
					
					int track;
					if (data.trackIndex == TRACK_VIDEO) {
						track = videoTrackIndex;
					} else {
						track = audioTrackIndex;
					}
					
					//添加数据

					mMuxerlock.lock();
					if (null != mediaMuxer) {
						if ((data.bufferInfo.flags&MediaCodec.BUFFER_FLAG_KEY_FRAME) == 1) {
							mbHasWriteIFrame = true;
						}
						
						mediaMuxer.writeSampleData(track, data.byteBuf, data.bufferInfo);
					}
					mMuxerlock.unlock();
				}
			}
		}
		
		Log.i(TAG, "run end");
	}
}
