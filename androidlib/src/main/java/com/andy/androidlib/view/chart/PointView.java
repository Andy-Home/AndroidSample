package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.andy.androidlib.view.Colors;
import com.andy.androidlib.view.DPValue;

public class PointView extends View {
    final String TAG = getClass().getSimpleName();
    public PointView(Context context) {
        super(context);
    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int p_x, p_y;
    public final static int radius = DPValue.dp2px(6);

    public void setPosition(int x, int y) {
        p_x = x;
        p_y = y;
    }

    public String x, y;

    public void setMessage(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public String msg = "";

    public void setMessage(String msg) {
        this.msg = msg;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Colors.LightGreen);
        canvas.drawCircle(radius, radius, radius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchListener.onTouch(index, event);
        return true;
    }

    private onTouchListener touchListener;
    private int index;

    public void setTouchListener(int index, onTouchListener listener) {
        touchListener = listener;
        this.index = index;
    }

    public interface onTouchListener {
        void onTouch(int index, MotionEvent event);
    }
}