package com.wmclient.capsdk;

import java.nio.ByteBuffer;
import android.media.MediaCodec.BufferInfo;

public class MuxerData {
	public int trackIndex = -1;
	public ByteBuffer byteBuf;
	public BufferInfo bufferInfo;
	
	public MuxerData(int trackIndex, ByteBuffer byteBuf, BufferInfo bufferInfo)
	{
		this.bufferInfo = bufferInfo;
		this.byteBuf = byteBuf;
		this.trackIndex = trackIndex;
	}
}
