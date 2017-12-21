package com.wmclient.clientsdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.MediaPlayer.PlayM4.Player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

public class HKPlayer implements IPlayer
{
	private Player m_playerInstance = Player.getInstance();
//	private IntercomSdk mIntercomSdk = null;
	
	private int m_nPort = -1;
	private static final int Errcode_Buf_Over = 11;	

	@Override
	public boolean IsPlaying()
	{
		return (m_nPort >= 0) ? true : false;
	}
	
	@Override
	public int StartPlay(byte[] pStreamHead, int nSize, int streamType, Object showObj)
	{
		int nMode = m_playerInstance.STREAM_REALTIME;
		
		switch(streamType)
		{
		case Constants.WMStreamType_RealTime :
			{
				nMode = m_playerInstance.STREAM_REALTIME;
			}
			break;
			
		case Constants.WMStreamType_File :
			{
				nMode = m_playerInstance.STREAM_FILE;
			}
			break;
			
		default:
			break;
		}
		
		int nPort = m_playerInstance.getPort();
		if(nPort < 0)
		{
			return Constants.fail;
		}
		else if (!m_playerInstance.setStreamOpenMode(nPort, nMode))
		{
			m_playerInstance.freePort(nPort);

			return Constants.fail;
		}
		else if(!m_playerInstance.setSecretKey(nPort, 1, "ge_security_3477".getBytes(), 128))
		{
			m_playerInstance.freePort(nPort);

			return Constants.fail;
		}		
		else if (!m_playerInstance.openStream(nPort, pStreamHead, nSize, 2*1024*1024))	//150K
		{
			m_playerInstance.freePort(nPort);

			return Constants.fail;
		}	
		else if(!m_playerInstance.play(nPort, (SurfaceHolder)showObj))
		{
			m_playerInstance.closeStream(m_nPort);
			m_playerInstance.freePort(nPort);
			
			return Constants.fail;
		}
		
		//set
		m_nPort = nPort;		
		
		return Constants.success;
	}
	
	@Override
	public int StopPlay()
	{
		if(m_nPort < 0)
		{
			return Constants.fail;
		}
		else if(!m_playerInstance.stop(m_nPort))
		{
			return Constants.fail;
		}
		else if(!m_playerInstance.closeStream(m_nPort))
		{
			//return Constants.fail;
		}	
		else if(m_playerInstance.freePort(m_nPort))
		{
			//return Constants;
		}
		
		//reset
		m_nPort = -1;
		
		return Constants.success;
	}

	@Override
	public int InputData(byte[] pBuf, int nSize)
	{
		if(m_nPort < 0)
		{
			return Constants.fail;
		}
		else if(!m_playerInstance.inputData(m_nPort, pBuf, nSize))
		{
			Log.e("inputData fail", "" + nSize);
			
			//check buffer over
			if(Errcode_Buf_Over == GetLastError())
			{
				return Constants.ErrorCode_PlayerBufOver;
			}
			
			return Constants.fail;
		}
		
		return Constants.success;
	}

	@Override
	public int PausePlay(int bPause)
	{
		if(m_nPort < 0)
		{
			return Constants.fail;
		}
		else if(!m_playerInstance.pause(m_nPort, bPause))
		{
			return Constants.fail;
		}
		
		return Constants.success;
	}

	@Override
	public int ControlFilePlay(int nControlCode, int nParam)
	{
		if(m_nPort < 0)
		{
			return Constants.fail;
		}
		
		switch(nControlCode)
		{
		case Constants.WMPlayControlCode_SpeedUp:
			{
				return (m_playerInstance.fast(m_nPort) ? Constants.success : Constants.fail);
			}

		case Constants.WMPlayControlCode_SpeedDown:
			{
				return (m_playerInstance.slow(m_nPort) ? Constants.success : Constants.fail);
			}

		default:
			break;
		}

		return Constants.success;
	}
	
	@Override
	public int GetPlaySpeed()
	{
		return Constants.success;
	}

	@Override
	public int GetPlayTime()
	{
		return Constants.success;
	}
	
	@Override
	public int SetPlayTime()
	{
		return Constants.success;
	}

	@Override
	public int OpenSound()
	{
		if(m_nPort < 0)
		{
			return Constants.fail;
		}
		else if (!m_playerInstance.playSound(m_nPort))
		{
			return Constants.fail;
		}

		return Constants.success;
	}
	
	@Override
	public int CloseSound()
	{
		if(m_nPort < 0)
		{
			return Constants.fail;
		}
		else if (!m_playerInstance.stopSound())
		{
			return Constants.fail;
		}

		return Constants.success;
	}

	@Override
	public int SetVolume(int nVolume)
	{
		return Constants.success;
	}
	
	@Override
	public int GetVolume()
	{
		return Constants.success;
	}

	@Override
	public int SaveSnapshot(String fileName, int nFormat)
	{
		if(Constants.WMSnapshotType_JPEG != nFormat)
		{
			return Constants.fail;
		}
		
		Player.MPInteger stWidth = new Player.MPInteger();
		Player.MPInteger stHeight = new Player.MPInteger();
		
	    if (!m_playerInstance.getPictureSize(m_nPort, stWidth, stHeight))
	    {
	    	return Constants.fail;
	    }
	    
	    int nSize = 5 * stWidth.value * stHeight.value;
	    byte[] picBuf = new byte[nSize];
	    Player.MPInteger stSize = new Player.MPInteger();
	    
	    //BMP picture
	    if(!m_playerInstance.getJPEG(m_nPort, picBuf, nSize, stSize))
	    {
	    	Log.e("SaveSnapshot", "getBMP failed with error code:" + m_playerInstance.getLastError(m_nPort));
	    	return Constants.fail;
	    }
	    
	    try
	    {
	    	File file = new File(fileName);
	    	file.createNewFile();
	    	
	    	if(!file.isFile()) 
	    	{
	    		return Constants.fail;
	    	}	    	
	    	
		    FileOutputStream fileStream = new FileOutputStream(file);
		    fileStream.write(picBuf, 0, stSize.value);
		    fileStream.close();
	    }
	    catch (Exception err)
		{
	    	return Constants.fail;
		}
	    
		return Constants.success;
	}

	@Override
	public int ResetSourceBuffer()
	{
		return Constants.success;
	}
	
	@Override
	public int GetSourceBufferSize()
	{
		return Constants.success;
	}	

	@Override
	public int StartVoiceTalk(AudioCaptureCallBack callBack)
	{
		return Constants.success;
	}	
	
	@Override
	public int StopVoiceTalk()
	{
		return Constants.success;
	}		
	
	@Override
	public int GetLastError()
	{
		return 0;
	}
}
