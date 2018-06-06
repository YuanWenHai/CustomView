package com.will.customview.view.circleloading;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * created  by will on 2018/6/6 9:47
 */
public class Timer {
    private OnFrameChangeListener mFrameListener;
    private OnSecondChangeListener mSecondListener;
    private OnTimerFinishListener mFinishListener;
    private Handler mHandler = new Handler(Looper.myLooper());
    private TimerRunnable mRunnable;
    private boolean isRunning;

    Timer(int countdownTime,int frameTime){
        mRunnable = new TimerRunnable(countdownTime,frameTime);
    }
    public void start(){
        if(isRunning){
            String message = "error,timer is already being started.";
            Log.e("Timer error",message);
            return;
        }
        mRunnable.reset();
        mHandler.post(mRunnable);
    }
    public void setCountdownTime(int time){
        mRunnable.setCountdownTime(time);
    }

    class TimerRunnable implements Runnable{
        private int frameTime;
        private int countdownTime;
        private int currentTime;
        TimerRunnable(int countdownTime, int frameTime){
            this.countdownTime = countdownTime;
            this.frameTime = frameTime;
        }
        private void reset(){
            currentTime = 0;
        }
        private void setCountdownTime(int time){
            if(isRunning){
                String message = "error,trying to set time during counting.";
                Log.e("Timer error",message);
                return;
            }
            countdownTime = time;
        }
        @Override
        public void run() {
            isRunning = true;
            if(mFrameListener != null){
                float fraction = (float)(currentTime)/(float)countdownTime;
                mFrameListener.onFrameChange(fraction);
            }
            if(mSecondListener != null && (countdownTime-currentTime)%1000<frameTime){
                mSecondListener.onSecondChange((countdownTime-currentTime)/1000);
            }
            if(currentTime < countdownTime){
                mHandler.postDelayed(this,frameTime);
            }else if(mFinishListener != null){
                isRunning = false;
                mFinishListener.onFinish();
            }
            currentTime+=frameTime;
        }
    }



    public void setFrameListener(OnFrameChangeListener listener) {
        mFrameListener = listener;
    }



    public void setSecondListener(OnSecondChangeListener listener) {
        mSecondListener = listener;
    }


    public void setFinishListener(OnTimerFinishListener listener) {
        mFinishListener = listener;
    }

    interface OnFrameChangeListener{
        void onFrameChange(float fraction);
    }
    interface OnSecondChangeListener {
        void onSecondChange(int sec);
    }
    interface OnTimerFinishListener{
        void onFinish();
    }
}
