package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.andy.androidlib.R;
import com.andy.androidlib.entity.PieContent;

import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图
 * <p>
 * Created by andy on 17-12-29.
 */

public class PieView extends View {
    private String TAG = getClass().getSimpleName();
    private int[] colors = {getResources().getColor(R.color.pie1), getResources().getColor(R.color.pie2),
            getResources().getColor(R.color.pie3), getResources().getColor(R.color.pie4), getResources().getColor(R.color.pie5),
            getResources().getColor(R.color.pie6), getResources().getColor(R.color.pie7), getResources().getColor(R.color.pie8),
            getResources().getColor(R.color.pie9), getResources().getColor(R.color.pie10),};

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
        setBackgroundColor(Color.WHITE);
        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Paint.Style.FILL);

        rectF = new RectF(10, 10, 100, 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (data != null && data.size() != 0) {
            float start = 0, percent;
            int flag = 0;
            for (PieContent content : data) {
                percent = content.getPercent() * 360;
                piePaint.setColor(colors[flag]);
                canvas.drawArc(rectF, start, percent, true, piePaint);
                flag++;
                Log.d(TAG, "颜色：" + colors[flag]);
                Log.d(TAG, "弧度：" + percent);
                start += percent;
            }

            //其他
            if (start < 360) {
                piePaint.setColor(colors[flag]);
                canvas.drawArc(rectF, start, 360 - start, true, piePaint);
            }
        }
    }

    private List<PieContent> data;

    /**
     * 设置显示数据
     *
     * @param contents 传入参数
     * @return 返回数据, 如果返回 null 值代表传入参数有问题,否则返回排序后的数据
     */
    public List<PieContent> setData(List<PieContent> contents) {
        float check = 0;
        for (PieContent content : contents) {
            check += content.getPercent();
        }
        if (check > 1f) {
            Log.e(TAG, "数据百分比相加大于1");
        } else {
            data = contents;
            if (check < 1f) {
                PieContent content = new PieContent();
                content.setContent("其他");
                content.setPercent(1f - check);
                data.add(content);
            }


            int minX = getWidth() / 8;
            int y = (getHeight() - (minX * 4)) / 2;
            rectF = new RectF(minX, y, getWidth() - 3 * minX, getHeight() - y);


            data = sort();
            return data;
        }
        return null;
    }

    private ArrayList<PieContent> sort() {
        ArrayList<PieContent> tmp = new ArrayList<>();
        tmp.addAll(data);
        int len = tmp.size();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                PieContent pre = tmp.get(i);
                PieContent suf = tmp.get(j);
                if (pre.getPercent() < suf.getPercent()) {
                    tmp.set(i, suf);
                    tmp.set(j, pre);
                }
            }
        }
        return tmp;
    }
}
