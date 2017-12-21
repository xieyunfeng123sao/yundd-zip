package com.vomont.yundudao.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 用于录制视频的工具类
 * 
 * @author 街角慢嗨
 * @email 773675907@qq.com
 * @time 2017-04-21
 */
@SuppressWarnings({"unused", "deprecation"})
public class MediaRecorderUtil
{
    
    private MediaRecorder mediaRecorder;
    
    // 录像camera
    private Camera camera;
    
    // 用于判断前置摄像头还是后置摄像头
    private int cameraPosition = 1;
    
    // 判断前置摄像头 还是后置
    private boolean isCheck;
    
    private SurfaceHolder.Callback callback;
    
    // 上下文
    private Context context;
    
    // 容器
    private SurfaceView surfaceView;
    
    private String filePath;
    
    OrientationEventListener orientationEventListener;
    
    boolean flagRecord = false;// 是否正在录像
    
    int rotationFlag = 90;
    
    int rotationRecord = 90;
    
    int frontRotate;
    
    int frontOri;
    
    int camaraType = 0;
    
    Activity activity;
    
    private int m, n;
    
    /**
     * 旋转界面UI
     */
    private void rotationUIListener()
    {
        orientationEventListener = new OrientationEventListener(context)
        {
            @Override
            public void onOrientationChanged(int rotation)
            {
                if (!flagRecord)
                {
                    if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330))
                    {
                        // 竖屏拍摄
                        if (rotationFlag != 0)
                        {
                            // 旋转logo
                            // rotationAnimation(rotationFlag, 0);
                            // 这是竖屏视频需要的角度
                            rotationRecord = 90;
                            // 这是记录当前角度的flag
                            rotationFlag = 0;
                        }
                    }
                    else if (((rotation >= 230) && (rotation <= 310)))
                    {
                        // 横屏拍摄
                        if (rotationFlag != 90)
                        {
                            // 旋转logo
                            // rotationAnimation(rotationFlag, 90);
                            // 这是正横屏视频需要的角度
                            rotationRecord = 0;
                            // 这是记录当前角度的flag
                            rotationFlag = 90;
                        }
                    }
                    else if (rotation > 30 && rotation < 95)
                    {
                        // 反横屏拍摄
                        if (rotationFlag != 270)
                        {
                            // 旋转logo
                            // rotationAnimation(rotationFlag, 270);
                            // 这是反横屏视频需要的角度
                            rotationRecord = 180;
                            // 这是记录当前角度的flag
                            rotationFlag = 270;
                        }
                    }
                }
            }
        };
        orientationEventListener.enable();
    }
    
    /**
     * 创建录制环境
     * 
     *            需要用到的surfaceview
     * @param context
     *            上下文
     */
    public void create(RelativeLayout relativeLayout, final Context context, Activity activity)
    {
        this.context = context;
        this.activity = activity;
        relativeLayout.removeAllViews();
        surfaceView=new SurfaceView(context);
        relativeLayout.addView(surfaceView);
//        this.surfaceView = surfaceView;
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setKeepScreenOn(true);
        callback = new SurfaceHolder.Callback()
        {
            // 在控件创建的时候，进行相应的初始化工作
            public void surfaceCreated(SurfaceHolder holder)
            {
                // 打开相机，同时进行各种控件的初始化mediaRecord等
                if (camera != null)
                {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
                try {
                    camera = Camera.open();
                }
                catch (Exception e)
                {
                    Toast.makeText(context,"没有权限,请打开录像权限！",Toast.LENGTH_SHORT).show();
                    return;
                }

                mediaRecorder = new MediaRecorder();
            }
            // 当控件发生变化的时候调用，进行surfaceView和camera进行绑定，可以进行画面的显示
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
                doChange(surfaceView.getHolder());
            }
            
            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                holder.removeCallback(this); 
                if (camera != null)
                {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            }
        };
      
        
      
        // 为SurfaceView设置回调函数
        surfaceView.getHolder().addCallback(callback);
        rotationUIListener();
    }
    
    // 当我们的程序开始运行，即使我们没有开始录制视频，我们的surFaceView中也要显示当前摄像头显示的内容
    private void doChange(SurfaceHolder holder)
    {
        try
        {
            if(camera!=null) {
                Camera.Parameters parameters = camera.getParameters();
                m = getCloselyPreSize(surfaceView.getWidth(), surfaceView.getHeight(), parameters.getSupportedPreviewSizes()).width;
                n = getCloselyPreSize(surfaceView.getWidth(), surfaceView.getHeight(), parameters.getSupportedPreviewSizes()).height;
                if (camaraType == 0) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
                    camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
                }
                if (rotationRecord == 90) {
                    parameters.setPreviewSize(getCloselyPreSize(surfaceView.getWidth(), surfaceView.getHeight(), parameters.getSupportedPreviewSizes()).width,
                            getCloselyPreSize(surfaceView.getWidth(), surfaceView.getHeight(), parameters.getSupportedPreviewSizes()).height);
                }

                camera.setParameters(parameters);
                camera.setPreviewDisplay(holder);
                // 设置surfaceView旋转的角度，系统默认的录制是横向的画面，把这句话注释掉运行你就会发现这行代码的作用
                if (camaraType == 1) {
                    frontCameraRotate();
                    camera.setDisplayOrientation(frontRotate);
                } else {
                    camera.setDisplayOrientation(90);
                }
                camera.startPreview();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 旋转前置摄像头为正的
     */
    private void frontCameraRotate()
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(1, info);
        int degrees = getDisplayRotation(activity);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        }
        else
        { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        frontOri = info.orientation;
        frontRotate = result;
    }
    
    /**
     * 获取旋转角度
     */
    private int getDisplayRotation(Activity activity)
    {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation)
        {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }
    
    /**
     * 切换摄像头
     */
    public void changeCamara()
    {
        // 切换前后摄像头
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
        for (int i = 0; i < cameraCount; i++)
        {
            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
            if (cameraPosition == 1)
            {
                // 现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                 // CAMERA_FACING_BACK后置
                    camera.stopPreview();// 停掉原来摄像头的预览
                    camera.release();// 释放资源
                    camera = null;// 取消原来摄像头
                    frontCameraRotate();// 前置旋转摄像头度数
                    camera = Camera.open(i);// 打开当前选中的摄像头
                    camaraType = i;
                    try
                    {
                        camera.setPreviewDisplay(surfaceView.getHolder());// 通过surfaceview显示取景画面
                        camera.setDisplayOrientation(frontRotate);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    camera.startPreview();// 开始预览
                    cameraPosition = 0;
                    isCheck = true;
                    break;
                }
            }
            else
            {
                // 现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                 // CAMERA_FACING_BACK后置
                    camera.stopPreview();// 停掉原来摄像头的预览
                    camera.release();// 释放资源
                    camera = null;// 取消原来摄像头
                    camera = Camera.open(i);// 打开当前选中的摄像头
                    camaraType = i;
                    cameraPosition = 1;
                    isCheck = false;
                    doChange(surfaceView.getHolder());
                    break;
                }
            }
        }
    }
    
    /**
     * 停止录制
     */
    public void stopRecord()
    {
        // 当结束录制之后，就将当前的资源都释放
        if (mediaRecorder != null)
        {
            mediaRecorder.release();
        }
        if (camera != null)
        {
            camera.stopPreview();
            camera.release();
        }
        camera = null;
        mediaRecorder = null;
    }
    
    /**
     * 重新开始的初始化方法
     */
    public void resCreate()
    {
        deleteFile(filePath);
        if (cameraPosition == 0)
        {
            camera = Camera.open(1);
            isCheck = true;
            camaraType = 1;
        }
        else
        {
            camera = Camera.open(0);
            camaraType = 0;
            isCheck = false;
        }
        // // 然后再重新初始化所有的必须的控件和对象
        
        mediaRecorder = new MediaRecorder();
        doChange(surfaceView.getHolder());
    }
    
    /**
     * 
     * 打开闪光灯的接口
     */
    @SuppressLint("NewApi")
    public void openMode(boolean isOpen)
    {
        Camera.Parameters p = camera.getParameters();
        if (isOpen)
        {
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        }
        else
        {
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
        }
        camera.setParameters(p);
    }
    
    /**
     * 
     * 视频镜头的伸缩
     * 改变zoom值 改变视频的伸缩距离
     * 
     * @param m 传入的值 不同手机支持的最大m不同
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("NewApi")
    public void setZoom(int m)
    {
        Camera.Parameters p = camera.getParameters();
        if (m <= 0)
        {
            p.setZoom(0);
        }
        else if (m >= p.getMaxZoom())
        {
            p.setZoom(p.getMaxZoom());
        }
        else
        {
            p.setZoom(m);
        }
        camera.setParameters(p);
    }
    
    /**
     * 
     * 是否支持视频镜头的伸缩
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isSupportZoom()
    {
        boolean isSuppport = true;
        if (camera.getParameters().isSmoothZoomSupported())
        {
            isSuppport = false;
        }
        return isSuppport;
    }
    
    /**
     * 
     * 不同的手机最大伸缩的值不一样
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int getMaxZoom()
    {
        if (camera != null)
        {
            Camera.Parameters p = camera.getParameters();
            return p.getMaxZoom();
        }
        return 0;
    }
    
    /**
     * 
     * 拍照
     * 
     * @param callBack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean TakePicture(PictureCallback callBack)
    {
        if (null == camera)
        {
            return false;
        }
        camera.takePicture(null, null, callBack);
        return true;
    }
    
    public void deleteVideo()
    {
        if (filePath != null)
        {
            File file = new File(filePath);
            if (file != null && file.exists() && file.isDirectory())
            {
                file.delete();
            }
        }
    }
    
    public static void deleteFile(String filePaths)
    {
        File file = new File(filePaths);
        if (file.exists() && file.isDirectory())
        {
            file.delete();
        }
    }
    
    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     * 
     * @param surfaceWidth
     *            需要被进行对比的原宽，surface view的宽度
     * @param surfaceHeight
     *            需要被进行对比的原高 surface view的高度
     * @param preSizeList 得到的支持预览尺寸的list，parmeters.getSupportedPreviewSizes()
     *            需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight, List<Size> preSizeList)
    {
        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (rotationRecord == 90)
        {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        }
        else
        {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        // 先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList)
        {
            if ((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight))
            {
                return size;
            }
        }
        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float)ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList)
        {
            curRatio = ((float)size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin)
            {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        return retSize;
    }
    
    /**
     * 开始录制
     * 
     * @param path
     *            录制保存的路径
     * @param name
     *            文件名称
     */
    public int startRecord(String path, String name)
    {
        // 先释放被占用的camera，在将其设置为mediaRecorder所用的camera
        // 解锁并将摄像头指向MediaRecorder
        if (camera != null)
            camera.unlock();
        if(camera==null)
        {
            return 0;
        }
        if(null==mediaRecorder)
        {
            Toast.makeText(context,"请打开录音权限！",Toast.LENGTH_SHORT).show();
            return 0;
        }
        mediaRecorder.setCamera(camera);
        // 指定输入源
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        
        // mediaRecorder.setCamera(camera)如果不用这个方式 那么下面这句话可以加入用来设置视频清晰度的 不然会产生冲突
        // 报错 我也不知道是为什么
        // mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        // 设置输出格式和编码格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 根据缓存 选择视频的比特率 从而改变清晰度
        ACache aCache = ACache.get(context);
        String av = aCache.getAsString("videosetting");
        if (!TextUtils.isEmpty(av))
        {
            if (av.equals("0"))
            {
                // 如果只设置比特率 不设置分辨率 清晰度是没有用的
                mediaRecorder.setVideoEncodingBitRate(56 * 8 * 1024);
                // 改成 640*480可以通用 但其他的 有些分辨率 有些手机是不支持的，所以最好不要定死，而是根据你录像窗口的
                // 的大小 选择一个最靠近的分辨率
                // 另外 应为camera的parameters.setPreviewSize（）设置了大小，所以如果不统一，录制出来的画面会有拉伸
                // 所以两边的分辨率最好统一起来
                mediaRecorder.setVideoSize(m, n);
            }
            else if (av.equals("1"))
            {
                mediaRecorder.setVideoEncodingBitRate(84 * 8 * 1024);
                mediaRecorder.setVideoSize(m, n);
            }
            else
            {
                mediaRecorder.setVideoEncodingBitRate(256 * 8 * 1024);
                mediaRecorder.setVideoSize(m, n);
            }
        }
        else
        {
            mediaRecorder.setVideoEncodingBitRate(56 * 8 * 1024);
            mediaRecorder.setVideoSize(m, n);
        }
        
        int frontRotation;
        if (rotationRecord == 180)
        {
            // 反向的前置
            frontRotation = 180;
        }
        else
        {
            // 正向的前置
            frontRotation = (rotationRecord == 0) ? 270 - frontOri : frontOri; // 录制下来的视屏选择角度，此处为前置
        }
        mediaRecorder.setOrientationHint((camaraType == 1) ? frontRotation : rotationRecord);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        // 还有个设置焦距的 发现没卵用 我就不加了 自己找去
        // 保存的路径以及文件名称
        filePath = path + "/" + name + ".mp4";
        mediaRecorder.setOutputFile(filePath);
        // 预览输出
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        // // 判断是前置摄像头还是后置摄像头 然后设置视频旋转 如果不加上 后置摄像头没有问题 但是前置摄像头录制的视频会导致上下翻转
        
        // 开始录制
        try
        {
            mediaRecorder.prepare();
            try {
                mediaRecorder.start();
            }
            catch (Exception e)
            {
                Toast.makeText(context,"请打开录音权限！",Toast.LENGTH_SHORT).show();
                return 0;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return  1;
    }
}
