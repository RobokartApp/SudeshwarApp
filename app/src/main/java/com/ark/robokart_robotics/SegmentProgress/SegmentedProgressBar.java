package com.ark.robokart_robotics.SegmentProgress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class SegmentedProgressBar extends View {
    private static final int FPS_IN_MILLI = 16;
    private static final String TAG = "SegmentedProgressBar";
    private float cornerRadius;
    private CountDownTimerWithPause countDownTimerWithPause;
    private int dividerCount = 0;
    private final Paint dividerPaint = new Paint();
    private List<Float> dividerPositions;
    private float dividerWidth = 1.0f;
    /* access modifiers changed from: private */
    public int[] gradientColors = new int[3];
    private boolean isDividerEnabled;
    private float lastDividerPosition;
    ProgressBarListener listener;
    /* access modifiers changed from: private */
    public long maxTimeInMillis;
    private float percentCompleted;
    /* access modifiers changed from: private */
    public int progressBarWidth;
    /* access modifiers changed from: private */
    public Paint progressPaint = new Paint();

    public SegmentedProgressBar(Context context) {
        super(context);
        init();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0.0f, 0.0f, this.percentCompleted, (float) getHeight());
        float f = this.cornerRadius;
        canvas.drawRoundRect(rectF, f, f, this.progressPaint);
        if (this.dividerCount > 0 && this.isDividerEnabled) {
            for (int i = 0; i < this.dividerCount; i++) {
                float leftPosition = this.dividerPositions.get(i).floatValue();
                canvas.drawRect(leftPosition, 0.0f, leftPosition + this.dividerWidth, (float) getHeight(), this.dividerPaint);
            }
        }
    }

    private void init() {
        this.dividerPositions = new ArrayList();
        this.cornerRadius = 0.0f;
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    SegmentedProgressBar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    SegmentedProgressBar segmentedProgressBar = SegmentedProgressBar.this;
                    int unused = segmentedProgressBar.progressBarWidth = segmentedProgressBar.getWidth();
                    Log.d(SegmentedProgressBar.TAG, "setShader: progressBarWidth : " + SegmentedProgressBar.this.progressBarWidth);
                    if (SegmentedProgressBar.this.gradientColors.length > 0) {
                        SegmentedProgressBar.this.progressPaint.setShader(new LinearGradient(0.0f, 0.0f, (float) SegmentedProgressBar.this.progressBarWidth, (float) SegmentedProgressBar.this.getHeight(), SegmentedProgressBar.this.gradientColors, null, Shader.TileMode.MIRROR));
                    }
                }
            });
        }
    }

    public void SetListener(ProgressBarListener listeter) {
        this.listener = listeter;
    }

    public void updateProgress(long millisPassed) {
        this.listener.TimeinMill(millisPassed);
        this.percentCompleted = (((float) this.progressBarWidth) * ((float) millisPassed)) / ((float) this.maxTimeInMillis);
        invalidate();
    }

    public void updateProgress(float percentValue) {
        this.percentCompleted = ((float) this.progressBarWidth) * percentValue;
        invalidate();
    }

    public void back_countdown(long timeinmillis) {
        this.countDownTimerWithPause.back_countdown(timeinmillis);
    }

    public void pause() {
        CountDownTimerWithPause countDownTimerWithPause2 = this.countDownTimerWithPause;
        if (countDownTimerWithPause2 == null) {
            Log.e(TAG, "pause: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
        } else {
            countDownTimerWithPause2.pause();
        }
    }

    public void resume() {
        CountDownTimerWithPause countDownTimerWithPause2 = this.countDownTimerWithPause;
        if (countDownTimerWithPause2 == null) {
            Log.e(TAG, "resume: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
        } else {
            countDownTimerWithPause2.resume();
        }
    }

    public void cancel() {
        CountDownTimerWithPause countDownTimerWithPause2 = this.countDownTimerWithPause;
        if (countDownTimerWithPause2 == null) {
            Log.e(TAG, "cancel: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
        } else {
            countDownTimerWithPause2.cancel();
        }
    }

    public void setShader(int[] colors) {
        this.gradientColors = colors;
        int i = this.progressBarWidth;
        if (i > 0) {
            this.progressPaint.setShader(new LinearGradient(0.0f, 0.0f, (float) i, (float) getHeight(), colors, null, Shader.TileMode.MIRROR));
        }
    }

    public void setDividerColor(int color) {
        this.dividerPaint.setColor(color);
    }

    public void setDividerWidth(float width) {
        if (width < 0.0f) {
            Log.w(TAG, "setDividerWidth: Divider width can not be negative");
        } else {
            this.dividerWidth = width;
        }
    }

    public void enableAutoProgressView(long timeInMillis) {
        if (timeInMillis < 0) {
            Log.w(TAG, "enableAutoProgressView: Time can not be in negative");
            return;
        }
        this.maxTimeInMillis = timeInMillis;
        this.countDownTimerWithPause = new CountDownTimerWithPause(this.maxTimeInMillis, 16, false) {
            public void onTick(long millisUntilFinished) {
                SegmentedProgressBar.this.updateProgress(SegmentedProgressBar.this.maxTimeInMillis - millisUntilFinished);
            }

            public void onFinish() {
                SegmentedProgressBar segmentedProgressBar = SegmentedProgressBar.this;
                segmentedProgressBar.updateProgress(segmentedProgressBar.maxTimeInMillis);
            }
        }.create();
    }

    public void setDividerEnabled(boolean value) {
        this.isDividerEnabled = value;
    }

    public void addDivider() {
        float f = this.lastDividerPosition;
        float f2 = this.percentCompleted;
        if (f != f2) {
            this.lastDividerPosition = f2;
            this.dividerCount++;
            this.dividerPositions.add(Float.valueOf(f2));
            invalidate();
            return;
        }
        Log.w(TAG, "addDivider: Divider already added to current position");
    }

    public void removeDivider() {
        this.dividerCount--;
        List<Float> list = this.dividerPositions;
        list.remove(list.size() - 1);
        this.lastDividerPosition = this.percentCompleted;
        invalidate();
    }

    public void reset() {
        this.countDownTimerWithPause.cancel();
        enableAutoProgressView(this.maxTimeInMillis);
        List<Float> list = this.dividerPositions;
        list.removeAll(list);
        this.percentCompleted = 0.0f;
        this.lastDividerPosition = 0.0f;
        this.dividerCount = 0;
        invalidate();
    }

    public float GetPercentComplete() {
        return this.percentCompleted;
    }

    public void setProgressColor(int color) {
        this.progressPaint.setColor(color);
    }

    public void publishProgress(float value) {
        if (value < 0.0f || value > 1.0f) {
            Log.w(TAG, "publishProgress: Progress value can only be in between 0 and 1");
        } else {
            updateProgress(value);
        }
    }

    public void setCornerRadius(float cornerRadius2) {
        this.cornerRadius = cornerRadius2;
    }
}
