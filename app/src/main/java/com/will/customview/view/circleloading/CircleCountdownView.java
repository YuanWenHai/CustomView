package com.will.customview.view.circleloading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.will.customview.R;
import com.will.customview.utils.Utils;

/**
 * created  by will on 2018/6/5 14:34
 */
public class CircleCountdownView extends FrameLayout {

    public static final int FRAME_RATE_60 = 60;
    public static final int FRAME_RATE_30 = 30;
    private TextView textView;
    private CircleView circleView;
    private Timer mTimer;

    public CircleCountdownView(@NonNull Context context) {
        super(context);
    }

    public CircleCountdownView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public CircleCountdownView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleCountdownView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setCountdownFinishListener(final OnCountdownFinishListener listener){
        mTimer.setFinishListener(new Timer.OnTimerFinishListener() {
            @Override
            public void onFinish() {
                listener.onFinish();
            }
        });
    }

    public void setMainColor(@ColorInt int color){
        circleView.setMainColor(color);
    }
    public void setCountdownBarColor(@ColorInt int color){
        circleView.setCountdownBarColor(color);
    }

    /**
     *  set countdown time in second.
     * @param time time
     */
    public void setCountdownTime(int time){
        mTimer.setCountdownTime(time*1000);
    }
    public void start(){
        mTimer.start();
    }

    private void initialize(AttributeSet attrs){
         TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleCountdownView);
         int mainColor = ta.getColor(R.styleable.CircleCountdownView_mainColor, Color.GRAY);
         int countdownBarColor = ta.getColor(R.styleable.CircleCountdownView_countdownBarColor,Color.BLACK);
         int countdownBarWidth = ta.getDimensionPixelSize(R.styleable.CircleCountdownView_countdownBarWidth,Utils.Dp2Px(getContext(),2));
         //the input time unit is second
         int countdownTime = ta.getInteger(R.styleable.CircleCountdownView_countdownTime,5) * 1000;
         int textColor = ta.getColor(R.styleable.CircleCountdownView_textColor,Color.WHITE);
         int textSize = ta.getDimensionPixelSize(R.styleable.CircleCountdownView_textSize,20);
         mTimer = new Timer(countdownTime,1000/FRAME_RATE_60);
         ta.recycle();
         initializeCircleView(mainColor,countdownBarColor,countdownBarWidth,countdownTime);
         initializeTextView(textColor,textSize,countdownTime);
         addView(circleView);
         addView(textView);
    }
    private void initializeCircleView(@ColorInt int mainColor,@ColorInt int countdownBarColor,int countdownBarWidth,int countdownTime){
        circleView = new CircleView(getContext(),mainColor,countdownBarColor,countdownBarWidth,mTimer);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        circleView.setLayoutParams(params);

    }
    private void initializeTextView(@ColorInt int textColor,int textSize,int countdownTime){
        textView = new TextView(getContext());
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        textView.setLayoutParams(params);
        mTimer.setSecondListener(new Timer.OnSecondChangeListener() {
            @Override
            public void onSecondChange(int sec) {
                textView.setText(String.valueOf(sec));
            }
        });
        textView.setText(""+countdownTime/1000);
    }
    public interface OnCountdownFinishListener{
        void onFinish();
    }
}
