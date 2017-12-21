package com.vomont.yundudao.view.changeimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ChangeView extends ImageView {

	private boolean isPaintLine = true;

	private float downX;

	private float downY;

	private Canvas canvas;

	private Paint paint;

	public ChangeView(Context context) {
		super(context, null);
	}

	public ChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ChangeView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initPaint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;
		drawLine(canvas);
	}

	private void initPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(10);
	}

	public void setDrawLine(boolean isSure) {
		isPaintLine = isSure;
	}

	private void drawLine(Canvas canvas) {
		if (isPaintLine) {
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float upX = event.getX();
			float upY = event.getY();
			canvas.drawLine(downX, downY, upX, upY, paint);
			invalidate();
			downX = upX;
			downY = upY;
			Log.d("insert", "downX==" + downX + "==downY==" + downY + "=upX="
					+ upX + "=upY==" + upY);
			break;
		case MotionEvent.ACTION_UP:

			break;
		default:
			break;
		}

		return true;
	}

}
