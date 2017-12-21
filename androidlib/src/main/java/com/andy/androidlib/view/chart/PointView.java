package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PointView extends View {
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

    public void setPosition(int x, int y) {
        p_x = x;
        p_y = y;
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
        mPaint.setColor(Color.RED);
        canvas.drawCircle(4, 4, 4, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchListener.onTouch(index, event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("PointView", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("PointView", "ACTION_UP");
                break;
        }
        return true;
    }

    private onTouchListener touchListener;
    private int index;

    public void setTouchListener(int index, onTouchListener listener) {
        touchListener = listener;
        this.index = index;
    }

    public interface onTouchListener {
        public void onTouch(int index, MotionEvent event);
    }
}