package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * 饼状图
 * <p>
 * Created by andy on 17-12-29.
 */

public class PieView extends View {
    public PieView(Context context) {
        super(context);
        init();
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private RectF rectF;
    private Paint piePaint;

    private void init() {
        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setColor(0xFFA4C739);
        piePaint.setStyle(Paint.Style.FILL);
        rectF = new RectF(10, 10, 100, 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(rectF, 0, -10, true, piePaint);
        canvas.drawArc(rectF, -20, -50, true, piePaint);
    }

    private List<PieContent> data;

    public void setData(List<PieContent> contents) {
        data = contents;
    }

    public class PieContent {
        String content;

        float percent;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public float getPercent() {
            return percent;
        }
    }
}
