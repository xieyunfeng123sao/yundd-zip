package com.vomont.fileloadsdk;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import com.vomont.fileloadsdk.UpLoadUtil.BigFileCallback;
import com.vomont.fileloadsdk.UpLoadUtil.FileCallback;

public class WMFileLoadSdk
{
    public static WMFileLoadSdk wmFileLoadSdk;
    
    /**
     * 文件上傳服务器的ip
     */
    private String ip;
    
    /**
     * 文件上传服务器的端口
     */
    private int port;
    
    /**
     * 用户id
     */
    private int accountid;
    
    /**
     * 标识符
     */
    private String signature;
    
    private String url;
    
    /**
     * 文件上传的初始状态为0
     */
    public static final int WM_FILE_STATE_INIT = 0;
    
    /**
     * 文件正在上传
     */
    public static final int WM_FILE_STATE_LOADING = 1;
    
    /**
     * 暂停上传
     */
    public static final int WM_FILE_STATE_PAUSE = 2;
    
    private HashMap<Integer, UpLoadUtil> map = new HashMap<Integer, UpLoadUtil>();
    
    private Context context;
    
    EventBigFileCallback eventBigFileCallback;
    
    EventCallback eventCallback;
    
    public static WMFileLoadSdk getInstance()
    {
        if (wmFileLoadSdk == null)
            wmFileLoadSdk = new WMFileLoadSdk();
        return wmFileLoadSdk;
    }
    
    /**
     * 
     * 初始化文件上传
     * 
     * @param ip
     * @param port
     * @param accountid 用户id
     * @param signature 标志符
     */
    public void WM_VFile_Init(String ip, int port, int accountid, String signature, Context context)
    {
        this.ip = ip;
        this.port = port;
        this.accountid = accountid;
        this.context = context;
        url = "http://" + ip + ":" + port;
    }
    
    /**
     *
     * 文件上传
     *
     * @param filePath 文件全路径
     * @param callback
     * @return 文件标志id
     */
    public int WM_VFile_UploadBigFile(String filePath)
    {
        if (!fileExists(filePath))
        {
            if (eventBigFileCallback != null)
                eventBigFileCallback.OnFail(0, -1);
            // 文件不存在
            return 0;
        }
        UpLoadUtil upLoadUtil = new UpLoadUtil(context);
        upLoadUtil.setBigFileCall(new BigFileCallback()
        {
            @Override
            public void OnSucess(int fileIdx, String fileUri)
            {
                if (eventBigFileCallback != null)
                    eventBigFileCallback.OnSucess(fileIdx, fileUri);
            }

            @Override
            public void OnProgress(int fileIdx, int progress)
            {
                if (eventBigFileCallback != null)
                    eventBigFileCallback.OnProgress(fileIdx, progress);
            }

            @Override
            public void OnFail(int fileIdx, int errorId)
            {
                if (eventBigFileCallback != null)
                    eventBigFileCallback.OnFail(fileIdx, errorId);
            }
        });
        int fileIdx = upLoadUtil.uploadBigFile(url, accountid, 0, filePath);
        map.put(fileIdx, upLoadUtil);
        return fileIdx;
    }
    
    public boolean fileExists(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 小文件上传
     * 
     * @param filePath 文件的全路径名
     * @param eventCallback
     * @return 文件标识id
     */
    public int WM_VFile_UploadFile(String filePath)
    {
        if (!fileExists(filePath))
        {
            if (eventCallback != null)
                eventCallback.OnFail(0, -1);
            // 文件不存在
            return 0;
        }
        UpLoadUtil upLoadUtil = new UpLoadUtil(context);
        upLoadUtil.setFileCall(new FileCallback()
        {
            @Override
            public void OnSucess(int fileIdx, String fileUri)
            {
                if (eventCallback != null)
                    eventCallback.OnSucess(fileIdx, fileUri);
            }
            
            @Override
            public void OnFail(int fileIdx, int errorId)
            {
                if (eventCallback != null)
                    eventCallback.OnFail(fileIdx, errorId);
            }
        });
        int fileIdx = upLoadUtil.upLoadFile(url, accountid, 0, filePath);
        map.put(fileIdx, upLoadUtil);
        return fileIdx;
    }
    
    /**
     * 
     * 暂停下载
     * 
     * @param fileIndx 文件标识id
     * @param bStop 是否暂停 true是暂停 false是继续下载
     * @return 0 成功1失败
     */
    public int WM_VFile_PauseUpload(int fileIndx, boolean bStop)
    {
        if (map != null && map.get(fileIndx) != null)
        {
            map.get(fileIndx).pauseUpload(fileIndx, url, accountid, bStop);
            return 0;
        }
        else
        {
            UpLoadUtil util = new UpLoadUtil(context);
            util.setBigFileCall(new BigFileCallback()
            {
                @Override
                public void OnSucess(int fileIdx, String fileUri)
                {
                    if (eventBigFileCallback != null)
                        eventBigFileCallback.OnSucess(fileIdx, fileUri);
                }
                
                @Override
                public void OnProgress(int fileIdx, int progress)
                {
                    if (eventBigFileCallback != null)
                    {
                        eventBigFileCallback.OnProgress(fileIdx, progress);
                    }
                     
                }
                
                @Override
                public void OnFail(int fileIdx, int errorId)
                {
                    if (eventBigFileCallback != null)
                        eventBigFileCallback.OnFail(fileIdx, errorId);
                }
            });
            
            int m = util.pauseNullUpload(fileIndx, url, accountid, bStop);
            map.put(fileIndx, util);
            return m;
        }
    }
    
    /**
     * 
     * 停止文件的上传(删除服务器上的文件分片)
     * 
     * @param fileIndx 文件标志id
     * @return 0成功 1失败
     */
    public void WM_VFile_StopUpload(int fileIndx)
    {
        if (map != null && map.get(fileIndx) != null)
        {
            map.get(fileIndx).stopFragment(accountid, fileIndx, url, eventCallback);
        }
        else
        {
            UpLoadUtil util = new UpLoadUtil(context);
            util.stopFragment(accountid, fileIndx, url, eventCallback);
        }
    }
    
    /**
     * 刪除已上传文件
     * 
     * @param fileUri 已上传文件的url
     * @param eventCallback
     */
    public void WM_VFile_DeleteFile(String fileUri)
    {
        UpLoadUtil util = new UpLoadUtil(context);
        util.deleteFile(accountid, url, fileUri, eventCallback);
    }
    
    public void setEventBigFileCallback(EventBigFileCallback eventBigFileCallback)
    {
        this.eventBigFileCallback = eventBigFileCallback;
    }
    
    public void setEventCallback(EventCallback eventCallback)
    {
        this.eventCallback = eventCallback;
    }
    
    public interface EventBigFileCallback
    {
        /**
         * 上传成功
         * 
         * @param fileIdx 文件id
         * @param fileUri 上传后url
         */
        void OnSucess(int fileIdx, String fileUri);
        
        /**
         * 上传出错的回调
         * 
         * @param fileIdx
         * @param errorId
         *            -1 文件不存在
         *            -2 网络问题
         *            2 参数错误 重新调用继续续传
         *            4 文件不存在 需要重新上传
         *            7 9不用管 服务器读写的问题
         */
        void OnFail(int fileIdx, int errorId);
        
        /**
         * 上传的进度
         * 
         * @param fileIdx 文件id
         * @param progress 文件的上传的进度
         */
        void OnProgress(int fileIdx, int progress);
    }
    
    public interface EventCallback
    {
        /**
         * 上传成功
         * 
         * @param fileIdx 文件id
         * @param fileUri 上传后url
         */
        void OnSucess(int fileIdx, String fileUri);
        
        /**
         * 上传出错的回调
         * 
         * @param fileIdx
         * @param errorId
         *            -1 文件不存在
         *            -2 网络问题
         *            2 参数错误 重新调用继续续传
         *            4 文件不存在 需要重新上传
         *            7 9不用管 服务器读写的问题
         */
        void OnFail(int fileIdx, int errorId);
    }
}
