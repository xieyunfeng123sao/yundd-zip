package com.vomont.fileloadsdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.base64.CCode;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vomont.fileloadsdk.WMFileLoadSdk.EventCallback;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

@SuppressLint("UseSparseArrays") public class UpLoadUtil
{
    // 分片的大小
    private int forSize;
    
    // 缓存
    private ACache aCache;
    
    // 文件全路径名称
    private String filepath;
    
    // 最大线程
    private int maxThreadSum = 4;
    
    // 分片的大小
    public static int fragmentSize = 1024 * 200;
    
    private final OkHttpClient client = new OkHttpClient();
    
    // 文件的信息
    private FileLoad bean;
    
    // 上传文件标识的集合
    private HashMap<Integer, String> positionList;
    
    // 文件的标识
    private int fileId;
    
    // 上传文件缓存信息的集合
    private List<String> nameList;
    
    BigFileCallback bigFileCallback;
    
    FileCallback fileCallback;
    
    private boolean bStop;
    
    public boolean isLoading;
    
    public UpLoadUtil(Context context)
    {
        aCache = ACache.get(context);
    }
    
    /**
     * 分片数量的计算
     * 
     * @param filePath
     * @throws IOException
     */
    public void cutFileUpload()
        throws IOException
    {
        File file = new File(filepath);
        if (!file.exists())
            throw new IOException("This is Empty File with " + filepath);
        forSize = (int)((file.length() % fragmentSize == 0) ? file.length() / fragmentSize : (file.length() / fragmentSize + 1));
    }
    
    /**
     * 判断该文件是否已经上传
     * 
     * @param filepath
     * @return
     * @throws IOException
     */
    public boolean hasLoad()
    {
        bean = (FileLoad)aCache.getAsObject(fileName());
        if (bean != null)
            // 判断文件是否已经上传
            return true;
        return false;
    }
    
    private String fileName()
    {
        String[] str = filepath.split("/");
        // 获取文件名称
        String fileName = str[str.length - 1];
        return fileName;
    }
    
    /**
     * 判断文件是否在上传
     * 
     * @return
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    public boolean isLoading()
        throws IOException
    {
        String[] str = filepath.split("/");
        if (str == null || str.length == 0)
            throw new IOException("This is not a File");
        // 获取文件名称
        String fileName = str[str.length - 1];
        bean = (FileLoad)aCache.getAsObject(fileName + fileId);
        if (bean.getLoadState() == 1)
            return true;
        return false;
    }
    
    /**
     * 上传大文件
     * 
     * @param url
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public int uploadBigFile(String url, int accountid, int fileIdx, String filepath)
    {
        this.filepath = filepath;
        if (fileIdx == 0)
        {
            // id为0说明是第一次 就赋值一个id给该文件
            positionList = (HashMap<Integer, String>)aCache.getAsObject("fileidxs");
            // 第一个id为1 之后的在此基础上加1
            if (positionList == null)
            {
                positionList = new HashMap<Integer, String>();
                fileIdx = 1;
            }
            else
            {
                fileIdx = positionList.size() + 1;
            }
            positionList.put(fileIdx, fileName());
            aCache.put("fileidxs", (Serializable)positionList);
            // 将上传文件信息缓存本地
            bean = new FileLoad();
            bean.setHasPack(false);
            bean.setLoadState(0);
            bean.setName(fileName());
            bean.setPath(filepath);
            bean.setFileIdx(fileIdx);
            aCache.put(fileName() + fileIdx, bean);
            //
            nameList = (List<String>)aCache.getAsObject("files");
            if (nameList == null)
            {
                nameList = new ArrayList<String>();
            }
            fileId = fileIdx;
            nameList.add(fileName() + fileId);
            aCache.put("files", (Serializable)nameList);
        }
        else
        {
            fileId = fileIdx;
            bean = (FileLoad)aCache.getAsObject(fileName() + fileIdx);
        }
        if (bean == null || bean.getPath() == null)
            return 0;
        try
        {
            // 计算分片大小
            cutFileUpload();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        threadLoad(url, accountid);
        return fileIdx;
    }
    
    public void threadLoad(String url, int accountid)
    {
        for (int i = 0; i < maxThreadSum; i++)
        {
            isLoading = true;
            uploadFragment(url, i, accountid);
        }
    }
    
    /**
     * 上传分片
     * 
     * @param url 上传的服务器
     * @param sendPosition 分片的下标
     * @param threadId 线程的下标
     */
    public synchronized void uploadFragment(final String url, final int sendPosition, final int accountid)
    {
        if (sendPosition >= forSize)
        {
            // 递归的数据都结束了
            if (bean.getMap() != null && (bean.getMap().size() == forSize))
            {
                aCache.put(fileName() + fileId, bean);
                // 所有的都已经上传了
                pack(url, accountid);
            }
            else if (bean.getMap() != null && (bean.getMap().size() < forSize))
            {
                aCache.put(fileName() + fileId, bean);
            }
            // 如果position越界了分片的最大值就不继续执行了
            return;
        }
        
        new Thread(new Runnable()
        {
            public void run()
            {
                if (bean != null && bean.getMap() != null && bean.getMap().containsKey(sendPosition))
                {
                    uploadFragment(url, sendPosition + maxThreadSum, accountid);
                }
                else
                {
                    if (loadFragment(sendPosition, accountid, url) && !bStop)
                    {
                        uploadFragment(url, sendPosition + maxThreadSum, accountid);
                    }
                    
                }
            }
        }).start();
    }
    
    @SuppressLint("UseSparseArrays") @SuppressWarnings("resource")
    private boolean loadFragment(int sendPosition, int accountid, String url)
    {
        if (bean != null && bean.getMap() != null && bean.getMap().containsKey(sendPosition))
        {
            return true;
        }
        RandomAccessFile randomAccessFile = null;
        try
        {
            // 分片
            randomAccessFile = new RandomAccessFile(filepath, "r");
            byte[] upcontent = null;
            // 这一段用的c++封装的base64编码 而不是系统的编码
            CCode cCode = new CCode();
            int size = 1024 * 1024;
            byte[] outcontent = new byte[size];
            byte[] buff = new byte[fragmentSize];
            randomAccessFile.seek(sendPosition * fragmentSize);
            int length = randomAccessFile.read(buff);
            byte[] buffer = new byte[length];
            if (sendPosition == (forSize - 1))
            {
                // 最后一个只取有用的部分
                System.arraycopy(buff, 0, buffer, 0, length);
            }
            else
            {
                buffer = buff;
            }
            int nCode = cCode.encode(buffer, buffer.length, outcontent, size);
            upcontent = new byte[nCode];
            System.arraycopy(outcontent, 0, upcontent, 0, nCode);
            final Map<String, String> params = new HashMap<String, String>();
            params.put("msgid", "257");
            params.put("accountid", accountid + "");
            params.put("fragmentid", sendPosition + "");
            params.put("fragmentcontent", new String(upcontent));
            params.put("fragmentlen", upcontent.length + "");
            StringBuffer sb = new StringBuffer();
            // 设置表单参数
            for (String key : params.keySet())
            {
                sb.append(key + "=" + params.get(key) + "&");
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sb.toString());
            Request request = new Request.Builder().url(url).post(body).build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful())
            {
                Gson gson = new Gson();
                String callback = response.body().string();
                Log.e("UpLoadUtil",callback);
                FragmentCallBack fragmentCallBack = gson.fromJson(callback, FragmentCallBack.class);
                if (fragmentCallBack != null && (fragmentCallBack.getResult() == 0 || fragmentCallBack.getResult() == 3))
                {
                    // 上传成功 && !bean.getMap().containsKey(sendPosition)
                    if (bean != null && bean.getMap() != null)
                    {
                        bean.getMap().put(sendPosition, fragmentCallBack.getFragmentcontext());
                    }
                    else if (bean != null && bean.getMap() == null)
                    {
                        HashMap<Integer, String> map = new HashMap<Integer, String>();
                        map.put(sendPosition, fragmentCallBack.getFragmentcontext());
                        bean.setMap(map);
                    }
                    synchronized (aCache)
                    {
                        Log.e("UpLoadUtil", bean.getMap().size() + "=====loadFragment=========");
                        aCache.put(fileName() + fileId, bean);
                    }
                    if (bigFileCallback != null)
                        bigFileCallback.OnProgress(fileId, bean.getMap().size() * 100 / forSize);
                }
                else
                {
                    if (fragmentCallBack != null && bigFileCallback != null)
                        bigFileCallback.OnFail(fileId, fragmentCallBack.getResult());
                    // 上传失败
                }
                return true;
            }
            else
            {
                if (bigFileCallback != null)
                    bigFileCallback.OnFail(fileId, -2);
                return false;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            if (bigFileCallback != null)
                bigFileCallback.OnFail(fileId, -1);
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if (bigFileCallback != null)
                bigFileCallback.OnFail(fileId, -2);
            return false;
        }
    }
    
    private void pack(String url, int accountid)
    {
        // 暂时这里用的okhttp 前面的 asynchttp的都需要改了 有问题的
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        String filetype = "0";
        if (filepath.endsWith(".mp4"))
        {
            filetype = "3";
        }
        
        if (filepath.endsWith(".jpg"))
        {
            filetype = "2";
        }
        if (filepath.endsWith(".png"))
        {
            filetype = "1";
        }
        String contexts = "";
        for (int i = 0; i < bean.getMap().size(); i++)
        {
            if (bean.getMap().containsKey(i))
            {
                contexts = contexts + bean.getMap().get(i) + ",";
            }
        }
        RequestBody requestBody;
        try
        {
            requestBody = new FormEncodingBuilder().add("msgid", 259 + "").add("accountid", accountid + "").add("contextlist", contexts).add("filetype", filetype).build();
            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onResponse(Response arg0)
                    throws IOException
                {
                    try
                    {
                        String name = arg0.body().string();
                        Log.e("UpLoadUtil",name);
                        JSONObject json = new JSONObject(name);
                        if (json.getInt("result") == 0 || json.getInt("result") == 3)
                        {
                            bean.setFileUri(json.getString("fileuri"));
                            aCache.put(fileName() + fileId, bean);
                            if (bigFileCallback != null)
                                bigFileCallback.OnSucess(fileId, json.getString("fileuri"));
                        }
                        else
                        {
//                            String errorFragment = json.getString("errorfragment");
                            aCache.put(fileName() + fileId, bean);
                            if (bigFileCallback != null)
                                bigFileCallback.OnFail(fileId, json.getInt("result"));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                
                @Override
                public void onFailure(Request arg0, IOException arg1)
                {
                    if (bigFileCallback != null)
                        bigFileCallback.OnFail(fileId, -2);
                }
            });
        }
        catch (IllegalArgumentException e1)
        {
            e1.printStackTrace();
        }
        catch (SecurityException e1)
        {
            e1.printStackTrace();
        }
        catch (IllegalStateException e1)
        {
            e1.printStackTrace();
        }
    }
    
    /**
     * 上传小文件
     */
    @SuppressWarnings("unchecked")
    public int upLoadFile(final String url, final int accountid, int fileIdx, String filePath)
    {
        this.filepath = filePath;
        if (fileIdx == 0)
        {
            // id为0说明是第一次 就赋值一个id给该文件
            positionList = (HashMap<Integer, String>)aCache.getAsObject("fileidxs");
            // 第一个id为1 之后的在此基础上加1
            if (positionList == null)
            {
                positionList = new HashMap<Integer, String>();
                fileIdx = 1;
            }
            else
            {
                fileIdx = positionList.size() + 1;
            }
            positionList.put(fileIdx, fileName());
            aCache.put("fileidxs", (Serializable)positionList);
        }
        fileId = fileIdx;
        new Thread(new Runnable()
        {
            public void run()
            {
                loadFile(url, accountid);
            }
        }).start();
        
        return fileIdx;
    }
    
    /**
     * 停止分片的上传
     * 
     * @param accountid
     * @param fileIdx
     * @param url
     * @param eventCallback
     */
    public void stopFragment(final int accountid, final int fileIdx, final String url, final EventCallback eventCallback)
    {
        bStop = true;
        @SuppressWarnings("unchecked")
        HashMap<Integer, String> map = (HashMap<Integer, String>)aCache.getAsObject("fileidxs");
        if (map == null || map.get(fileId) == null)
        {
            if (eventCallback != null)
                eventCallback.OnSucess(fileIdx, "");
        }
        else
        {
            String name = map.get(fileId);
            final FileLoad fileLoad = (FileLoad)aCache.getAsObject(name + fileIdx);
            if (fileLoad == null || fileLoad.getMap() == null || fileLoad.getMap().size() == 0)
            {
                if (eventCallback != null)
                    eventCallback.OnSucess(fileIdx, "");
                return;
            }
            new Thread(new Runnable()
            {
                public void run()
                {
                    String contexts = "";
                    Set<Integer> sets = fileLoad.getMap().keySet();
                    for (Integer num : sets)
                    {
                        contexts = contexts + fileLoad.getMap().get(num) + ",";
                    }
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("msgid", "258");
                    params.put("accountid", accountid + "");
                    params.put("fragmentcontent", contexts);
                    StringBuffer sb = new StringBuffer();
                    // 设置表单参数
                    for (String key : params.keySet())
                    {
                        sb.append(key + "=" + params.get(key) + "&");
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sb.toString());
                    Request request = new Request.Builder().url(url).post(body).build();
                    Response response;
                    try
                    {
                        response = client.newCall(request).execute();
                        if (response.isSuccessful())
                        {
                            String callback = response.body().string();
                            JSONObject json = new JSONObject(callback);
                            if (json.getInt("result") == 0 || json.getInt("result") == 5)
                            {
                                if (eventCallback != null)
                                    eventCallback.OnSucess(fileIdx, "");
                            }
                            else
                            {
                                if (eventCallback != null)
                                    eventCallback.OnFail(fileIdx, json.getInt("result"));
                            }
                            
                        }
                        else
                        {
                            if (eventCallback != null)
                                // 上传失败
                                eventCallback.OnFail(fileIdx, -2);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        if (eventCallback != null)
                            eventCallback.OnFail(fileIdx, -2);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        if (eventCallback != null)
                            eventCallback.OnFail(fileIdx, -2);
                    }
                }
            }).start();
        }
    }
    
    public void deleteFile(final int accountid, final String url, final String fileUri, final EventCallback eventCallback)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("msgid", "261");
                params.put("accountid", accountid + "");
                params.put("fileuri", fileUri);
                StringBuffer sb = new StringBuffer();
                // 设置表单参数
                for (String key : params.keySet())
                {
                    sb.append(key + "=" + params.get(key) + "&");
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sb.toString());
                Request request = new Request.Builder().url(url).post(body).build();
                Response response;
                try
                {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        String callback = response.body().string();
                        JSONObject json = new JSONObject(callback);
                        if (json.getInt("result") == 0 || json.getInt("result") == 4)
                        {
                            if (eventCallback != null)
                                eventCallback.OnSucess(0, "");
                        }
                        else
                        {
                            if (eventCallback != null)
                                eventCallback.OnFail(0, json.getInt("result"));
                        }
                    }
                    else
                    {
                        if (eventCallback != null)
                            // 上传失败
                            eventCallback.OnFail(0, -2);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    if (eventCallback != null)
                        eventCallback.OnFail(0, -2);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    if (eventCallback != null)
                        eventCallback.OnFail(0, -2);
                }
            }
        }).start();
    }
    
    private void loadFile(String url, int accountid)
    {
        String filetype = "0";
        if (filepath.endsWith(".mp4"))
        {
            filetype = "3";
        }
        
        if (filepath.endsWith(".jpg"))
        {
            filetype = "2";
        }
        if (filepath.endsWith(".png"))
        {
            filetype = "1";
        }
        RandomAccessFile randomAccessFile = null;
        try
        {
            // 分片
            randomAccessFile = new RandomAccessFile(filepath, "r");
            byte[] upcontent = null;
            // 这一段用的c++封装的base64编码 而不是系统的编码
            CCode cCode = new CCode();
            int size = 1024 * 1024;
            byte[] outcontent = new byte[size];
            byte[] buff = new byte[fragmentSize];
            randomAccessFile.seek(0);
            int length = randomAccessFile.read(buff);
            byte[] buffer = new byte[length];
            // 最后一个只取有用的部分
            System.arraycopy(buff, 0, buffer, 0, length);
            int nCode = cCode.encode(buffer, buffer.length, outcontent, size);
            upcontent = new byte[nCode];
            System.arraycopy(outcontent, 0, upcontent, 0, nCode);
            final Map<String, String> params = new HashMap<String, String>();
            params.put("msgid", "260");
            params.put("accountid", accountid + "");
            params.put("filecontent", new String(upcontent));
            params.put("filetype", filetype);
            params.put("filelen", upcontent.length + "");
            
            StringBuffer sb = new StringBuffer();
            // 设置表单参数
            for (String key : params.keySet())
            {
                sb.append(key + "=" + params.get(key) + "&");
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sb.toString());
            Request request = new Request.Builder().url(url).post(body).build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful())
            {
                Gson gson = new Gson();
                String callback = response.body().string();
                FragmentCallBack fragmentCallBack = gson.fromJson(callback, FragmentCallBack.class);
                if (fragmentCallBack != null && (fragmentCallBack.getResult() == 0 || fragmentCallBack.getResult() == 3))
                {
                    if (fileCallback != null)
                        fileCallback.OnSucess(fileId, fragmentCallBack.getFileuri());
                }
                else
                {
                    if (fragmentCallBack != null && fileCallback != null)
                        fileCallback.OnFail(fileId, fragmentCallBack.getResult());
                    // 上传失败
                }
            }
            else
            {
                if (fileCallback != null)
                    fileCallback.OnFail(fileId, -2);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            if (fileCallback != null)
                fileCallback.OnFail(fileId, -1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if (fileCallback != null)
                fileCallback.OnFail(fileId, -2);
        }
    }
    
    /**
     * 暂停
     * 
     * @param url
     * @param accountid
     * @param bStop
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void pauseUpload(int fileIdx, String url, int accountid, boolean bStop)
    {
        if (fileId == 0 && fileIdx != 0)
        {
            this.fileId = fileIdx;
        }
        this.bStop = bStop;
        if (bStop)
        {
            isLoading = false;
            if (bean != null)
                aCache.put(fileName() + fileId, bean);
        }
        else if (!isLoading)
        {
            if (filepath == null)
            {
                positionList = (HashMap<Integer, String>)aCache.getAsObject("fileidxs");
                if (positionList != null && positionList.get(fileIdx) != null)
                {
                    bean = (FileLoad)aCache.getAsObject(positionList.get(fileIdx) + fileIdx);
                    if (bean == null)
                        return;
                    filepath = bean.getPath();
                    File file = new File(filepath);
                    if (!file.exists())
                        return;
                }
            }
            try
            {
                // 计算分片大小
                cutFileUpload();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (bean.getFileUri() != null)
            {
                if (bigFileCallback != null)
                    bigFileCallback.OnSucess(fileId, bean.getFileUri());
            }
            {
                threadLoad(url, accountid);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public int pauseNullUpload(int fileIdx, String url, int accoutid, boolean bStop)
    {
        if (fileId == 0 && fileIdx != 0)
        {
            this.fileId = fileIdx;
        }
        this.bStop = bStop;
        if (bStop)
        {
            isLoading = false;
        }
        else if (!isLoading)
        {
            positionList = (HashMap<Integer, String>)aCache.getAsObject("fileidxs");
            if (positionList != null && positionList.get(fileIdx) != null)
            {
                bean = (FileLoad)aCache.getAsObject(positionList.get(fileIdx) + fileIdx);
                if (bean == null)
                    return 1;
                filepath = bean.getPath();
                File file = new File(filepath);
                if (!file.exists())
                    return 1;
                if (bean.getFileUri() != null)
                {
                    if (bigFileCallback != null)
                        bigFileCallback.OnSucess(fileId, bean.getFileUri());
                }
                else
                {
                    threadLoad(url, accoutid);
                }
            }
            else
            {
                return 1;
            }
        }
        return 0;
    }
    
    public void setBigFileCall(BigFileCallback bigFileCallback)
    {
        this.bigFileCallback = bigFileCallback;
    }
    
    public void setFileCall(FileCallback fileCallback)
    {
        this.fileCallback = fileCallback;
    }
    
    public interface BigFileCallback
    {
        void OnSucess(int fileIdx, String fileUri);
        
        void OnFail(int fileIdx, int errorId);
        
        void OnProgress(int fileIdx, int progress);
    }
    
    public interface FileCallback
    {
        void OnSucess(int fileIdx, String fileUri);
        
        void OnFail(int fileIdx, int errorId);
    }
}
