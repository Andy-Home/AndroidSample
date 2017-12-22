package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class X_YView extends View{


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
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
    }

    public int padding = 15;

    public void setPadding(int padding){
        this.padding = padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        if(style == Style.NOT_VALUE){

        }else{
            if(style == Style.SX_SY){
                canvas.drawLine(padding,padding,padding,height- padding,mPaint);                //y
                canvas.drawLine(padding,height-padding,width-padding,height-padding,mPaint);    //x
            }else if(style == Style.SX_Y){
                canvas.drawLine(padding,padding,padding,height- padding,mPaint);                //y
                canvas.drawLine(padding,height/2,width-padding,height/2,mPaint);                //x
            }else if(style == Style.X_Y){
                canvas.drawLine(width/2, padding, width/2,height-padding,mPaint);               //y
                canvas.drawLine(padding,height/2,width-padding,height/2,mPaint);                //x
            }else if(style == Style.X_SY){
                canvas.drawLine(width/2, padding, width/2,height-padding,mPaint);               //y
                canvas.drawLine(padding,height-padding,width-padding,height-padding,mPaint);    //x
            }
        }
    }

    private int style;
    public void setStyle(int style){
        this.style = style;
    }

    public static class Style{
        public static int SX_SY = 1;
        public static int SX_Y = 2;
        public static int X_Y = 3;
        public static int X_SY = 4;
        public static int NOT_VALUE = 5;
    }
}
