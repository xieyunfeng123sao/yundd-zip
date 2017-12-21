package com.vomont.fileloadsdk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vomont.fileloadsdk.WMFileLoadSdk.EventBigFileCallback;
import com.vomont.fileloadsdk.WMFileLoadSdk.EventCallback;

public class MainActivity extends Activity
{
    public String base = Environment.getExternalStorageDirectory().getAbsolutePath();
    
    private TextView num;
    
    private Button pause;
    
    private Button stop;
    
    private Button delete;
    
    private boolean isStop;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num = (TextView)findViewById(R.id.num);
        pause = (Button)findViewById(R.id.pause);
        stop = (Button)findViewById(R.id.stop);
        delete = (Button)findViewById(R.id.delete);
        WMFileLoadSdk.getInstance().WM_VFile_Init("192.168.0.187", 9090, 15, "", this);
        WMFileLoadSdk.getInstance().setEventBigFileCallback(new EventBigFileCallback()
        {
            @Override
            public void OnSucess(int fileIdx, String fileUri)
            {
                Log.e("insert", "EventBigFileCallback=" + fileIdx + "=fileUri=" + fileUri);
            }
            
            @Override
            public void OnProgress(int fileIdx, int progress)
            {
                Log.e("insert", "EventBigFileCallback=" + fileIdx + "=progress=" + progress);
            }
            
            @Override
            public void OnFail(int fileIdx, int errorId)
            {
                Log.e("insert", "EventBigFileCallback=" + fileIdx + "=errorId=" + errorId);
            }
        });
        
        WMFileLoadSdk.getInstance().setEventCallback(new EventCallback()
        {
            
            @Override
            public void OnSucess(int fileIdx, String fileUri)
            {
                Log.e("insert", "EventCallback=" + fileIdx + "=fileUri=" + fileUri);
            }
            
            @Override
            public void OnFail(int fileIdx, int errorId)
            {
                Log.e("insert", "EventCallback=" + fileIdx + "=errorId=" + errorId);
            }
        });
        // 2017-09-13-16-52-28 2017-09-13-16-52-28.mp4
//        final int id = WMFileLoadSdk.getInstance().WM_VFile_UploadBigFile(base + "/zhangxun/yun-vedio/2017-09-13-16-52-28.mp4");
        final int id=0;
        pause.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (!isStop)
                {
                    WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(id, true);
                }
                else
                {
                    WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(id, false);
                }
                isStop = !isStop;
            }
        });
        
        stop.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WMFileLoadSdk.getInstance().WM_VFile_StopUpload(id);
            }
        });
        
        delete.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WMFileLoadSdk.getInstance().WM_VFile_DeleteFile("15-d502987ae4969b6a.mp4");
            }
        });
        
        // UpLoadUtil upLoadUtil = new UpLoadUtil(this, base + "/zhangxun/yun-vedio/2017-09-13-19-46-32.mp4");
        // // UpLoadUtil upLoadUtil = new UpLoadUtil(this, base + "/zhangxun/yundd-face/1.png");
        // upLoadUtil.uploadBigFile("http://192.168.0.187:9090", 15, 0);
        // UpLoadUtil upLoadUtil2 = new UpLoadUtil(this, base + "/zhangxun/yun-vedio/2017-09-13-19-46-32.mp4");
        // upLoadUtil2.uploadBigFile("http://192.168.0.187:9090", 15, 0);
        //
        // UpLoadUtil upLoadUtil3 = new UpLoadUtil(this, base + "/zhangxun/yun-vedio/2017-09-13-19-46-32.mp4");
        // upLoadUtil3.uploadBigFile("http://192.168.0.187:9090", 15, 0);
        //
        // UpLoadUtil upLoadUtil4 = new UpLoadUtil(this, base + "/zhangxun/yun-vedio/2017-09-13-19-46-32.mp4");
        // upLoadUtil4.uploadBigFile("http://192.168.0.187:9090", 15, 0);
        // upLoadUtil.upLoadFile("http://192.168.0.187:9090", 15, 0);
    }
}
