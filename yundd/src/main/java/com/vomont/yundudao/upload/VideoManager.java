package com.vomont.yundudao.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.ui.home.HomeActivity;
import com.vomont.yundudao.utils.ShareUtil;

import android.os.Environment;

public class VideoManager
{
    public static String base = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun";
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/yun-vedio";
    public static String toppath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/yun-top";
    public static String yasuo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/yun-yasuo";
    public static String compress = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/compress";
    public static String sendimg = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/yunimg";
    public static String yundd_phone = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/yundd-photo";
    public static String change_img = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/change_img";
    public static String detail_img_cash = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/yundd-cash";
    public static String video_face_img=Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhangxun/yundd-face";
    public static int maxTime=60*1000;
    public static List<byte[]> cutFileUpload(String filePath)
    {
        try
        {
            List<byte[]> mlist = new ArrayList<byte[]>();
            FileAccessI fileAccessI = new FileAccessI(filePath, 0);
            Long nStartPos = 0l;
            Long length = fileAccessI.getFileLength();
            int mBufferSize = 1024 * 100; 
            byte[] buffer = new byte[mBufferSize];
            FileAccessI.Detail detail;
            long nRead = 0l;
            long nStart = nStartPos;
            while (nStart < length)
            {
                detail = fileAccessI.getContent(nStart);
                nRead = detail.length;
                buffer = detail.b;
                nStart += nRead;
                nStartPos = nStart;
                mlist.add(buffer);
            }
            return mlist;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
