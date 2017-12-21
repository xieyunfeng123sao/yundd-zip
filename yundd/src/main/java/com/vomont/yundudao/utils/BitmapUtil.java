package com.vomont.yundudao.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class BitmapUtil
{
    public static byte[] bitmapToBytes(Bitmap bm)
    {
        byte[] bytes = null;
        if (bm != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }
    
    public static Bitmap Bytes2Bimap(byte[] b)
    {
        if (b.length != 0)
        {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        else
        {
            return null;
        }
    }
    
    public static void savePhotoByte(Bitmap photoBitmap, String path, String photoName)
    {
        
        File dir = new File(path.trim());
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        if (photoBitmap != null)
        {
            byte[] byteArray = bitmapToBytes(photoBitmap);
            File photoFile = new File(path.trim() + "/" + photoName);
            try
            {
                photoFile.createNewFile();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            FileOutputStream fileOutputStream = null;
            BufferedOutputStream bStream = null;
            try
            {
                fileOutputStream = new FileOutputStream(photoFile);
                bStream = new BufferedOutputStream(fileOutputStream);
                bStream.write(byteArray);
            }
            catch (FileNotFoundException e)
            {
                photoFile.delete();
                e.printStackTrace();
            }
            catch (IOException e)
            {
                photoFile.delete();
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (bStream != null)
                    {
                        bStream.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     * 
     * @param imagePath 图像的路径
     * @param width 指定输出图像的宽度
     * @param height 指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight)
        {
            be = beWidth;
        }
        else
        {
            be = beHeight;
        }
        if (be <= 0)
        {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    
    /**
     * @param path 原图路径
     * @param offsetX 截取开始点在X轴偏移量
     * @param offsetY 截取开始点在Y轴偏移量
     * @param targetW 截取多宽（像素）
     * @param targetH 截取多高（像素）
     * @return
     */
    public static Bitmap matrixScale(String path, int offsetX, int offsetY, int targetW, int targetH)
    {
        // 构建原始位图
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        // 获取原始宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算宽高缩放比例，targetW，targetH即期待缩放完成后位图的宽高
        float scaleW = (float)targetW / width;
        float scaleH = (float)targetH / height;
        // 将缩放比例放进矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);
        // 这个方法作用非常多，详细解释一下各个参数的意义！
        // bitmap：原始位图
        // 第二到第五个参数，即截取原图哪一部分构建新位图，
        // offsetX和offsetY代表在X轴和Y轴上的像素偏移量，即从哪个位置开始截取
        // width和height代表截取多少个像素，但是要注意，offsetX+width应该小于等于原图的宽度
        // offsetY+height小于等于原图高度，要不然会报错，因为截到原图外面去了
        // 像下面这样填写，就代表截取整个原图，
        // Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        // 如果填写100,100,200,200，就代表
        // 从原图左上角往右和下各偏移100像素，然后往后和往下各截取200构建新位图
        // matrix：缩放矩阵
        // 最后一个参数表示如果矩阵里面还存放了过滤条件，是否按条件过滤（如果matrix里面只放了平移数据），最后一个参数设置成什么都不会生效
        bitmap = Bitmap.createBitmap(bitmap, offsetX, offsetY, width, height, matrix, false);
        return bitmap;
    }
    
    /**
     * @param path 图片路径
     * @param quality 质量 0-100,100表示原图
     * @return
     */
    public static Bitmap losslessScale(String path, int quality)
    {
        double maxSize = 25000;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        Log.e("哈哈", "原始大小：" + baos.toByteArray().length);
        // 因为质量压缩不是可以无限缩小的，所以一张高质量的图片，再怎么压缩，
        // 最终size可能还是大于你指定的size，造成异常
        // 所以不建议循环压缩，而是指定quality，进行一次压缩就可以了
        while (baos.toByteArray().length > maxSize)
        {
            quality -= 10;
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, quality, baos);
            Log.e("哈哈", "过程中大小为：" + baos.toByteArray().length);
        }
        // bitmap.compress(CompressFormat.JPEG, quality, baos);
        
        Log.e("哈哈", "最终大小" + baos.toByteArray().length);
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
        return compressedBitmap;
    }
    
    // public static Bitmap getBitmapFromUri(Context context, Uri uri)
    // {
    // try
    // {
    // // 读取uri所在的图片
    //
    // Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    // return bitmap;
    // }
    // catch (Exception e)
    // {
    // Log.e("[Android]", e.getMessage());
    // Log.e("[Android]", "目录为：" + uri);
    // e.printStackTrace();
    // return null;
    // }
    // }
    
    /**
     * 通过uri获取图片并进行压缩
     * 
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri)
        throws FileNotFoundException, IOException
    {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;// optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        // 图片分辨率以480x800为标准
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = (int)(originalWidth / ww);
        }
        else if (originalWidth < originalHeight && originalHeight > hh)
        {// 如果高度高的话根据宽度固定大小缩放
            be = (int)(originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        // 比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;// 设置缩放比例
        bitmapOptions.inDither = true;// optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        
        return compressImage(bitmap);// 再进行质量压缩
    }
    
    /**
     * 读取图片的旋转的角度
     * 
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path)
    {
        int degree = 0;
        try
        {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return degree;
    }
    
    /**
     * 质量压缩方法
     * 
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            // 第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差 ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    
    // /**
    // * 通过Uri获取文件
    // *
    // * @param ac
    // * @param uri
    // * @return
    // */
    // public static File getFileFromMediaUri(Context ac, Uri uri)
    // {
    // if (uri.getScheme().toString().compareTo("content") == 0)
    // {
    // ContentResolver cr = ac.getContentResolver();
    // Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
    // if (cursor != null)
    // {
    // cursor.moveToFirst();
    // String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
    // cursor.close();
    // if (filePath != null)
    // {
    // return new File(filePath);
    // }
    // }
    // }
    // else if (uri.getScheme().toString().compareTo("file") == 0)
    // {
    // return new File(uri.toString().replace("file://", ""));
    // }
    // return null;
    // }
    
    public static File getRealPathFromURI(Context ac, Uri contentUri)
    {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ac.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst())
        {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return new File(res);
    }
    
    /**
     * 加载本地图片
     * 
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        
        return BitmapFactory.decodeFile(url, options);
    }
    
    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        
        if (height > reqHeight || width > reqWidth)
        {
            final int heightRatio = Math.round((float)height / (float)reqHeight);
            final int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    
    /**
     * 将图片按照某个角度进行旋转
     * 
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree)
    {
        Bitmap returnBm = null;
        
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try
        {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        catch (OutOfMemoryError e)
        {
        }
        if (returnBm == null)
        {
            returnBm = bm;
        }
        if (bm != returnBm)
        {
            bm.recycle();
        }
        return returnBm;
    }
    
    /**
     * 读取照片exif信息中的旋转角度
     * 
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path)
    {
        int degree = 0;
        try
        {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return degree;
    }
    
    public static Bitmap getBitRGB(String path)
    {
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(path, options2);
        return bitmap;
    }
    
    /**
     * 获取本地视频的截图
     * 
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                retriever.release();
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    
    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * 
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind)
    {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w" + bitmap.getWidth());
        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    
    public static Bitmap imageZoom(Bitmap bitMap)
    {
        // 图片允许最大空间 单位：KB
        double maxSize = 20.00;
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize)
        {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i), bitMap.getHeight() / Math.sqrt(i));
        }
        
        return bitMap;
    }
    
    /***
     * 图片的缩放方法
     * 
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight)
    {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int)width, (int)height, matrix, true);
        return bitmap;
    }
    
    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     * 
     * @param context The context.
     * @param uri The Uri to query.
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri)
    {
        
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                
                if ("primary".equalsIgnoreCase(type))
                {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri))
            {
                
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                
                Uri contentUri = null;
                if ("image".equals(type))
                {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type))
                {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type))
                {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }
        
        return null;
    }
    
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * 
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
    {
        
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        
        try
        {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst())
            {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    
}
