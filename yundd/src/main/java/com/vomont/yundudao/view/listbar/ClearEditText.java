package com.vomont.yundudao.view.listbar;

import com.vomont.yundudao.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

@SuppressLint("ClickableViewAccessibility")
public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher
{
    
    private Drawable mClearDrawable;
    
    public ClearEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init()
    {
        
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null)
        {
            mClearDrawable = getResources().getDrawable(R.drawable.emotionstore_progresscancelbtn);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        
        setClearIconVisible(false);
        
        setOnFocusChangeListener(this);
        
        addTextChangedListener(this);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (getCompoundDrawables()[2] != null)
        {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable)
                {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }
    
    public ClearEditText(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.editTextStyle);
    }
    
    public ClearEditText(Context context)
    {
        this(context, null);
    }
    
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
    {
        setClearIconVisible(text.length() > 0);
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        
    }
    
    @Override
    public void afterTextChanged(Editable s)
    {
        
    }
    
    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            setClearIconVisible(getText().length() > 0);
        }
        else
        {
            setClearIconVisible(false);
        }
    }
    
    protected void setClearIconVisible(boolean visible)
    {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
    
    public void setShakeAnimation()
    {
        this.setAnimation(shakeAnimation(5));
    }
    
    public static Animation shakeAnimation(int counts)
    {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
}
