package com.vomont.yundudao.ui.createproblem.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CavansView extends View {

	/**
	 * 绘制线条的Paint,即用户手指绘制Path
	 */
	private Paint mOutterPaint = new Paint();
	/**
	 * 记录用户绘制的Path
	 */
	private Path mPath = new Path();
	/**
	 * 内存中创建的Canvas
	 */
	private Canvas mCanvas;
	/**
	 * mCanvas绘制内容在其上
	 */
	private Bitmap mBitmap;

	private int mLastX;
	private int mLastY;

	private int x, y;

	@SuppressWarnings("unused")
	private boolean isSure = true;
	// 保存的路径
	private ArrayList<PosPath> savePath;
	// 删除的路径
	private ArrayList<PosPath> deletePath;
	// 路径
	private PosPath dp;

	@SuppressWarnings("unused")
	private int LINE_TYPE = 0;

	@SuppressWarnings("unused")
	private int CIRCLE = 1;

	private int type;

	private int circleR = 100;

	public void setType(int type) {
		this.type = type;
	}

	public CavansView(Context context) {
		super(context, null);
	}

	public CavansView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public CavansView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 初始化画布和画笔
	 */
	public void init() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		// 初始化bitmap
		mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		// 设置画笔
		mOutterPaint.setColor(Color.RED);
		mOutterPaint.setStrokeWidth(10);// 画笔的粗细
		mOutterPaint.setStyle(Paint.Style.STROKE);
		mOutterPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
		mOutterPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
	}

	public void setOnPaint(boolean isSure) {
		this.isSure = isSure;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		init();
		savePath = new ArrayList<PosPath>();
		deletePath = new ArrayList<PosPath>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawPath();
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	/**
	 * 绘制线条
	 */
	private void drawPath() {
		if (mPath != null)
			mCanvas.drawPath(mPath, mOutterPaint);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		x = (int) event.getX();
		y = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (type == 0) {
				addPos();
			} else {
				init();
				addCircle();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (type == 0) {
				movePos();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (type == 0) {
				upPos();
			}
			break;
		}
		invalidate();
		return true;
	}

	/**
	 * 添加涂鸦的线条
	 */
	private void addPos() {
		// 画的路线
		mPath = new Path();
		dp = new PosPath();
		dp.path = mPath;
		dp.paint = mOutterPaint;
		mPath.reset();// 清空path
		mLastX = x;
		mLastY = y;
		// 画笔的初始位置
		mPath.moveTo(mLastX, mLastY);
	}

	/**
	 * 添加涂鸦的线条
	 */
	private void movePos() {
		int dx = Math.abs(x - mLastX);
		int dy = Math.abs(y - mLastY);
		if (dx > 3 || dy > 3)
			// 如果移动大于3像素 就划线
			mPath.lineTo(x, y);
		mLastX = x;
		mLastY = y;
	}

	private void upPos() {
		mPath.lineTo(x, y);
		mCanvas.drawPath(mPath, mOutterPaint);
		savePath.add(dp);
		mPath = null;
	}

	/**
	 * 撤销 上一步
	 * 
	 * 清空画布 讲 集合draw
	 */
	public void forBack() {
		if (type == 0) {
			if (savePath != null && savePath.size() > 0) {
				// 初始化 画布 就等于清空画布
				init();

				// 将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
				PosPath posPath = savePath.get(savePath.size() - 1);
				deletePath.add(posPath);
				savePath.remove(savePath.size() - 1);

				// 将路径保存列表中的路径重绘在画布上
				Iterator<PosPath> iter = savePath.iterator(); // 重复保存
				while (iter.hasNext()) {
					PosPath dp = iter.next();
					mCanvas.drawPath(dp.path, dp.paint);
				}

			}
		} else {
			init();
			addPos();
		}
		invalidate();// 刷新
	}

	/**
	 * 将撤销的路径再回复
	 */
	public void backToNow() {
		if (type == 0) {
			if (deletePath != null && deletePath.size() > 0) {
				init();

				PosPath posPath = deletePath.get(deletePath.size() - 1);
				savePath.add(posPath);
				deletePath.remove(deletePath.size() - 1);

				Iterator<PosPath> iter = savePath.iterator();
				while (iter.hasNext()) {
					PosPath dp = iter.next();
					mCanvas.drawPath(dp.path, dp.paint);
				}
			}
		} else {
			init();
			addCircle();
		}
		invalidate();

	}

	/**
	 * 添加圆
	 */
	public void addCircle() {
		addPos();
		mPath = new Path();
		mPath.addCircle(x, y, circleR, Direction.CCW);
		invalidate();
	}

	/**
	 * 路径的对象
	 * 
	 * @author Administrator
	 */
	class PosPath {
		Path path;
		Paint paint;
	}

	/**
	 * 组合涂鸦图片和源图片
	 * 
	 * @param src
	 *            源图片
	 * @param watermark
	 *            涂鸦图片
	 * @return 组合后的bitmap
	 * 
	 */
	public Bitmap doodle(Bitmap src) {
	    
//	    int degree = getExifOrientation(path);
//        if(degree == 90 || degree == 180 || degree == 270){
//            //Roate preview icon according to exif orientation
//            Matrix matrix = new Matrix();
//            matrix.postRotate(degree);
//            b = Bitmap.createBitmap(bitmap, 0, 0, outWidth, outHeight, matrix, true);
//        }else{
//            //do not need roate the icon,default
//        }

	    
		// 另外创建一张图片
		Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas canvas = new Canvas(newb);
		canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
		canvas.drawBitmap(
				resizeImage(mBitmap, src.getWidth(), src.getHeight()), 0, 0,
				null); // 涂鸦图片画到原图片中间位置
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		mBitmap.recycle();
		mBitmap = null;
		return newb;
	}
	
	
	
	public static int getExifOrientation(String filepath) {
	       int degree = 0;
	       ExifInterface exif = null;

	       try {
	           exif = new ExifInterface(filepath);
	       } catch (IOException ex) {
	          // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
	       }

	       if (exif != null) {
	           int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	           if (orientation != -1) {
	               // We only recognize a subset of orientation tag values.
	               switch (orientation) {
	               case ExifInterface.ORIENTATION_ROTATE_90:
	                   degree = 90;
	                   break;

	               case ExifInterface.ORIENTATION_ROTATE_180:
	                   degree = 180;
	                   break;

	               case ExifInterface.ORIENTATION_ROTATE_270:
	                   degree = 270;
	                   break;
	               default:
	                   break;
	               }
	           }
	       }

	       return degree;
	   }

	// 使用Bitmap加Matrix来缩放

	/**
	 * 缩放bitmap到指定大小
	 * 
	 * @param bitmap
	 *            需要缩放的bitmap
	 * @param w
	 *            缩放后的宽度
	 * @param h
	 *            缩放后的高度
	 * @return
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	/**
	 * 保存图片的
	 */
	public void saveBitmap(Bitmap bm, String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(dir);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
