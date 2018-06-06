package com.will.customview.view.circleloading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.will.customview.utils.Utils;

/**
 * created  by will on 2018/6/5 11:57
 */
    class CircleView extends View {
    private Paint mMainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCountdownBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final int MIN_HEIGHT_IN_DP = 100;
    private static final int MIN_WIDTH_IN_DP = 100;

    private int minHeightInPx;
    private int minWidthInPx;

    private int countdownBarWidth;

    private Timer sharedTimer;

    private float circleFraction;




    public CircleView(Context context, @ColorInt int mainColor,@ColorInt int countdownBarColor,int countdownBarWidth,Timer sharedTimer){
        super(context);
        mMainPaint.setColor(mainColor);
        mCountdownBarPaint.setColor(countdownBarColor);
        this.countdownBarWidth = Utils.Dp2Px(getContext(),countdownBarWidth);
        this.sharedTimer = sharedTimer;
        initialize();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }
    void setMainColor(@ColorInt int color){
        mMainPaint.setColor(color);
        invalidate();
    }
    void setCountdownBarColor(@ColorInt int color){
        mCountdownBarPaint.setColor(color);
        invalidate();
    }

    private void initialize(){
        sharedTimer.setFrameListener(new Timer.OnFrameChangeListener() {
            @Override
            public void onFrameChange(float fraction) {
                circleFraction = fraction;
                invalidate();
            }
        });
        minWidthInPx = Utils.Dp2Px(getContext(),MIN_WIDTH_IN_DP);
        minHeightInPx = Utils.Dp2Px(getContext(),MIN_HEIGHT_IN_DP);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
        drawCircle(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams params = getLayoutParams();
        int defaultWidth = minWidthInPx;
        int defaultHeight = minHeightInPx;
        if(params.width == ViewGroup.LayoutParams.WRAP_CONTENT || params.height == ViewGroup.LayoutParams.WRAP_CONTENT){
            if(params.width == ViewGroup.LayoutParams.WRAP_CONTENT && params.height == ViewGroup.LayoutParams.WRAP_CONTENT){
                defaultWidth = minWidthInPx;
                defaultHeight = minHeightInPx;
            }else if(params.height == ViewGroup.LayoutParams.WRAP_CONTENT){
                defaultHeight = minHeightInPx;
            }else{
                defaultWidth = minWidthInPx;
            }
        }
        int desiredWidth = defaultWidth + getPaddingLeft() + getPaddingRight();
        int desiredHeight = defaultHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec), measureDimension(desiredHeight, heightMeasureSpec));
    }


    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        if (result < desiredSize){
            Log.e("Circle view", "The view is too small, the content might get cut");
        }
        return result;
    }

    private void drawArc(Canvas canvas){
        float degree = circleFraction * 360f;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int diam = Math.min(width,height);
        int left = width > height ? (width-height)/2 : 0;
        int top = width > height ? 0 : (height-width)/2;
        canvas.drawArc(left,top,left+diam,top+diam,-90,degree,true,mCountdownBarPaint);
    }
    private void drawCircle(Canvas canvas){
        int cx = getMeasuredWidth()/2;
        int cy = getMeasuredHeight()/2;
        int rarius = (Math.min(getMeasuredHeight(),getMeasuredWidth())/2) - countdownBarWidth;
        canvas.drawCircle(cx,cy,rarius,mMainPaint);
    }
}
