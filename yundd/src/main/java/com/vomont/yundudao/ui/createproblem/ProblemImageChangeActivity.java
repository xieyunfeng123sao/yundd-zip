package com.vomont.yundudao.ui.createproblem;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.createproblem.view.CavansView;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.addpic.LocalMedia;
import com.vomont.yundudao.utils.addpic.LocalMediaFolder;

@SuppressLint("ClickableViewAccessibility")
public class ProblemImageChangeActivity extends Activity implements OnClickListener, OnTouchListener
{
    
    private ImageView changeimg_back;
    
    private TextView changeimg_name;
    
    private CavansView change_view;
    
    private ImageView problem_change_img;
    
    private ImageView changeimg_sure;
    
    private RadioButton changeimg_paint;
    
    private RadioButton changeimg_circle;
    
    private RadioButton changeimg_backoff;
    
    private RadioButton changeimg_goahead;
    
    private Intent intent;
    
    private LocalMedia localMedia;
    
    private String imgUrl;
    
    
    private List<LocalMediaFolder> mfolders;
    
    private int position;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        initView();
        initData();
        initListener();
    }
    
    private void initView()
    {
        changeimg_back = (ImageView)findViewById(R.id.changeimg_back);
        changeimg_name = (TextView)findViewById(R.id.changeimg_name);
        changeimg_sure = (ImageView)findViewById(R.id.changeimg_sure);
        changeimg_paint = (RadioButton)findViewById(R.id.changeimg_paint);
        changeimg_circle = (RadioButton)findViewById(R.id.changeimg_circle);
        changeimg_backoff = (RadioButton)findViewById(R.id.changeimg_backoff);
        changeimg_goahead = (RadioButton)findViewById(R.id.changeimg_goahead);
        change_view = (CavansView)findViewById(R.id.change_view);
        problem_change_img = (ImageView)findViewById(R.id.problem_change_img);
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        GlideCacheUtil.getInstance().getCacheSize(this);
        changeimg_name.setText("涂鸦");
        intent = getIntent();
        mfolders = (List<LocalMediaFolder>)intent.getSerializableExtra("LocalMediaFolder");
        position = intent.getIntExtra("position", 0);
        localMedia = (LocalMedia)intent.getSerializableExtra("changeimg");
        imgUrl = localMedia.getPath();
//        Glide.with(ProblemImageChangeActivity.this).load(new File(imgUrl)).into(problem_change_img);
        ImageLoader.getInstance().displayImage("file://"+imgUrl, problem_change_img);
        Matrix matrix = problem_change_img.getImageMatrix();
        RectF rectF = new RectF();
        Drawable drawable = problem_change_img.getDrawable();
        if (drawable != null)
        {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
    }
    
    private void initListener()
    {
        changeimg_back.setOnClickListener(this);
        changeimg_sure.setOnClickListener(this);
        changeimg_paint.setOnClickListener(this);
        changeimg_circle.setOnClickListener(this);
        changeimg_backoff.setOnClickListener(this);
        changeimg_goahead.setOnClickListener(this);
        change_view.setOnTouchListener(this);
    }
    
    @SuppressLint("SimpleDateFormat")
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.changeimg_back:
                intent.putExtra("hasChange", 0);
                finish();
                break;
            case R.id.changeimg_sure:
                Bitmap bitmap2 = change_view.doodle(BitmapUtil.getLoacalBitmap(imgUrl));
                String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-0-0-0";
                String url = VideoManager.change_img + "/" + name + ".jpg";
                change_view.saveBitmap(bitmap2, url);
                if (mfolders != null)
                    for (int i = 0; i < mfolders.get(position).getImages().size(); i++)
                    {
                        if (mfolders.get(position).getImages().get(i).getPath().equals(imgUrl))
                        {
                            mfolders.get(position).getImages().get(i).setPath(url);
                        }
                    }
                localMedia.setPath(url);
                bitmap2.recycle();
                intent.putExtra("LocalMediaFolder", (Serializable)mfolders);
                intent.putExtra("changeimg", localMedia);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.changeimg_paint:
                change_view.forBack();
                change_view.setType(0);
                break;
            case R.id.changeimg_circle:
                change_view.setType(1);
                change_view.forBack();
                break;
            case R.id.changeimg_backoff:
                change_view.forBack();
                break;
            case R.id.changeimg_goahead:
                change_view.backToNow();
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return false;
    }
    
}
