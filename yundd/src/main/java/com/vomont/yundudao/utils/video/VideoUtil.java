package com.vomont.yundudao.utils.video;

import java.io.File;


public class VideoUtil
{
    /**
     * 
     * @param videoPath 源文件路径
     * @param savePath 保存视频的路径
     * @param startS  开始时间   （秒）
     * @param endS    时间长度   （秒）
     */
    public static void cutVideo(String videoPath,String savePath,final int startS,final int endS) {
   
   
        final File file = new File(videoPath); // 视频文件地址
        final File trimFile = new File(savePath);// 裁剪文件保存地址
        // 进行裁剪
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { // 开始裁剪
                    TrimVideoUtils.getInstance().startTrim(true, startS, endS, file, trimFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 设置回调为null
                    TrimVideoUtils.getInstance().setTrimCallBack(null);
                }
            }
        }).start();
    }
}
