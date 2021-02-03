package com.ark.robokart_robotics.SegmentProgress;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.ark.robokart_robotics.VideoRecord.Variables;

public abstract class CountDownTimerWithPause {
    private static final int MSG = 1;
    /* access modifiers changed from: private */
    public final long mCountdownInterval;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            synchronized (CountDownTimerWithPause.this) {
                long millisLeft = CountDownTimerWithPause.this.timeLeft();
                if (millisLeft <= 0) {
                    CountDownTimerWithPause.this.cancel();
                    CountDownTimerWithPause.this.onFinish();
                } else if (millisLeft < CountDownTimerWithPause.this.mCountdownInterval) {
                    sendMessageDelayed(obtainMessage(1), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    CountDownTimerWithPause.this.onTick(millisLeft);
                    long delay = CountDownTimerWithPause.this.mCountdownInterval - (SystemClock.elapsedRealtime() - lastTickStart);
                    while (delay < 0) {
                        delay += CountDownTimerWithPause.this.mCountdownInterval;
                    }
                    sendMessageDelayed(obtainMessage(1), delay);
                }
            }
        }
    };
    private long mMillisInFuture;
    private long mPauseTimeRemaining;
    private final boolean mRunAtStart;
    private long mStopTimeInFuture;
    private final long mTotalCountdown;

    public abstract void onFinish();

    public abstract void onTick(long j);

    public CountDownTimerWithPause(long millisOnTimer, long countDownInterval, boolean runAtStart) {
        this.mMillisInFuture = millisOnTimer;
        this.mTotalCountdown = this.mMillisInFuture;
        this.mCountdownInterval = countDownInterval;
        this.mRunAtStart = runAtStart;
    }

    public final void cancel() {
        this.mHandler.removeMessages(1);
    }

    public final synchronized CountDownTimerWithPause create() {
        if (this.mMillisInFuture <= 0) {
            onFinish();
        } else {
            this.mPauseTimeRemaining = this.mMillisInFuture;
        }
        if (this.mRunAtStart) {
            resume();
        }
        return this;
    }

    public void pause() {
        if (isRunning()) {
            this.mPauseTimeRemaining = timeLeft();
            String str = Variables.tag;
            Log.d(str, "mPauseTimeRemaining " + (this.mPauseTimeRemaining / 1000));
            cancel();
        }
    }

    public void resume() {
        if (isPaused()) {
            this.mMillisInFuture = this.mPauseTimeRemaining;
            this.mStopTimeInFuture = SystemClock.elapsedRealtime() + this.mMillisInFuture;
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(1));
            this.mPauseTimeRemaining = 0;
            String str = Variables.tag;
            Log.d(str, "mStopTimeInFuture " + (this.mStopTimeInFuture / 1000));
        }
    }

    public void back_countdown(long backtime) {
        this.mPauseTimeRemaining += backtime;
    }

    public boolean isPaused() {
        return this.mPauseTimeRemaining > 0;
    }

    public boolean isRunning() {
        return !isPaused();
    }

    public long timeLeft() {
        if (isPaused()) {
            return this.mPauseTimeRemaining;
        }
        long millisUntilFinished = this.mStopTimeInFuture - SystemClock.elapsedRealtime();
        if (millisUntilFinished < 0) {
            return 0;
        }
        return millisUntilFinished;
    }

    public long totalCountdown() {
        return this.mTotalCountdown;
    }

    public long timePassed() {
        return this.mTotalCountdown - timeLeft();
    }

    public boolean hasBeenStarted() {
        return this.mPauseTimeRemaining <= this.mMillisInFuture;
    }
}
