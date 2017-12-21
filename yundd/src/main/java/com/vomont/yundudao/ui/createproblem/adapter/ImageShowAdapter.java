package com.vomont.yundudao.ui.createproblem.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.upload.VideoManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

@SuppressLint({"SimpleDateFormat", "InflateParams"})
public class ImageShowAdapter extends BaseAdapter
{
    private List<String> mlist;
    
    private Context context;
    
//    private OnCallBack onCallBack;
    
    String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    
    public ImageShowAdapter(Context context, List<String> mlist)
    {
        this.context = context;
        this.mlist = mlist;
    }
    
    @Override
    public int getCount()
    {
        
        return null != mlist ? mlist.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return mlist.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageShowHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_problem_img, null);
            holder = new ImageShowHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.item_problem_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ImageShowHolder)convertView.getTag();
        }
        for(int i=0;i<mlist.size();i++)
        {
            if(Integer.parseInt(mlist.get(i).substring(14, 15))==position)
            {
                
                Glide.with(context).load(new File(VideoManager.detail_img_cash + "/" + mlist.get(i) + ".jpg")).into( holder.img);
//                LoadLocalImageUtil.getInstance().displayFromSDCard(VideoManager.detail_img_cash + "/" + mlist.get(i) + ".jpg", holder.img);
                break;
            }
        }
        
        return convertView;
    }
    
//    private synchronized void asyncloadImage(final ImageView imageView, final String imagecontent, final int position)
//    {
//        final Handler mHandler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg)
//            {
//                super.handleMessage(msg);
//                if (msg.what == 1)
//                {
//                    String path = (String)msg.obj;
//                    if (imageView != null && imagecontent != null)
//                    {
//                        File file = new File(path);
//                        if (file.exists())
//                        {
//                            LoadLocalImageUtil.getInstance().displayFromSDCard(path, imageView);
//                        }
//                        
//                        if (onCallBack != null)
//                        {
//                            onCallBack.getPath(path, position);
//                        }
//                    }
//                }
//            }
//        };
//        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
//        Runnable runnable = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                try
//                {
//                    String mPath = VideoManager.detail_img_cash + "/" + name + position + ".jpg";
//                    File file = new File(mPath);
//                    // 这个URI是图片下载到本地后的缓存目录中的URI
//                    if (imagecontent != null && !file.exists())
//                    {
//                        ImageUtils.byte2File(Base64.decode(URLDecoder.decode(imagecontent, "utf-8"), Base64.DEFAULT), VideoManager.detail_img_cash, name + position + ".jpg");
//                        Message msg = new Message();
//                        msg.what = 1;
//                        msg.obj = mPath;
//                        mHandler.sendMessage(msg);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        };
//        new Thread(runnable).start();
//    }
    
    public void setOnCallBackListener(OnCallBack onCallBack)
    {
//        this.onCallBack = onCallBack;
    }
    
    public interface OnCallBack
    {
        void getPath(String path, int position);
    }
    
    public Bitmap stringtoBitmap(String string)
    {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            
            // 解决加载图片 内存溢出的问题
            // Options 只保存图片尺寸大小，不保存图片到内存
            BitmapFactory.Options opts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
            opts.inSampleSize = 2;
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length, opts);
            // 回收
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return bitmap;
    }
    
    class ImageShowHolder
    {
        ImageView img;
    }
    
}
