package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.andy.androidlib.view.Colors;
import com.andy.androidlib.view.DPValue;

public class X_YView extends View{
    final String TAG = getClass().getSimpleName();
    public X_YView(Context context) {
        super(context);
        init();
    }

    public X_YView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public X_YView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mPaint;

    private void init(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(DPValue.dp2px(1));
        mPaint.setColor(Colors.RoyalBlue);
    }

    public int padding = 0;

    public void setPadding(int padding){
        this.padding = padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        if (style == Style.NOT_VALUE) {

        }else{
            if(style == Style.SX_SY){
                canvas.drawLine(origin.x, padding ,origin.x,origin.y,mPaint);                //y
                canvas.drawLine(origin.x,origin.y,width-padding,origin.y,mPaint);    //x
            }else if(style == Style.SX_Y){
                canvas.drawLine(origin.x, padding, origin.x, height - padding, mPaint);                 //y
                canvas.drawLine(origin.x, origin.y, width - padding, origin.y, mPaint);                //x
            }else if(style == Style.X_Y){
                canvas.drawLine(origin.x, padding, origin.x, height - padding, mPaint);               //y
                canvas.drawLine(padding, origin.y, width - padding, origin.y, mPaint);                 //x
            }else if(style == Style.X_SY){
                canvas.drawLine(origin.x, padding, origin.x, origin.y, mPaint);               //y
                canvas.drawLine(padding, origin.y, width - padding, origin.y, mPaint);    //x
            }
        }
    }

    public int style;
    private Point origin = new Point(0, 0);

    public void setStyle(int style, Point origin) {
        this.style = style;
        this.origin = origin;
    }

    public static class Style{
        static int SX_SY = 1;
        static int SX_Y = 2;
        static int X_Y = 3;
        static int X_SY = 4;
        static int NOT_VALUE = 5;
    }
}
