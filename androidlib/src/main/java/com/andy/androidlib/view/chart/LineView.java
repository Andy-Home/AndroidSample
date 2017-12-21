package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class LineView extends ViewGroup implements PointView.onTouchListener {
    public LineView(Context context) {
        super(context);
        setBackgroundColor(Color.GRAY);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.GRAY);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.GRAY);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int index = 0; index < getChildCount(); index++) {
            View v = getChildAt(index);
            if (v instanceof PointView) {
                int x = ((PointView) v).p_x;
                int y = ((PointView) v).p_y;
                v.layout(x - 4, y - 4, x + 4, y + 4);
            } else {
                v.layout(l, t, r, b);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        int height = getHeight() - 10;
        int width = getWidth() - 10;
        //y轴
        canvas.drawLine(12, 12, 12, height, mPaint);
        //x轴
        canvas.drawLine(12, height, width, height, mPaint);

        if (touchIndex != -1) {
            PointView v = (PointView) getChildAt(touchIndex);
            canvas.drawText("x:" + v.p_x + " y:" + v.p_y, v.p_x, v.p_y - 9, mPaint);
        }

        for (int i = 0; i < pointViewList.size() - 1; i++){
            PointView start = pointViewList.get(i);
            PointView end = pointViewList.get(i + 1);
            canvas.drawLine(start.p_x, start.p_y, end.p_x, end.p_y, mPaint);
        }

    }

    private ArrayList<Point> pointTable;

    public void setPoints(ArrayList<Point> points) {
        if (points == null) {
            points = new ArrayList<>();
            points.add(new Point(100, 900));
            points.add(new Point(140, 150));
            points.add(new Point(180, 700));
            points.add(new Point(220, 110));
            points.add(new Point(260, 130));
            points.add(new Point(300, 10));
            points.add(new Point(340, 130));
        }
        pointTable = points;
        analysis();
    }

    private ArrayList<PointView> pointViewList = new ArrayList<>();

    private void analysis() {
        int minX = -1, minDistanceX = 0, maxY = -1;
        Point dis = null;
        for (Point p : pointTable) {
            if (minX == -1) {
                minX = p.x;
            }
            if (maxY < p.y) {
                maxY = p.y;
            }
            if (dis == null) {
                dis = p;
            } else {
                if (Math.abs(p.x - dis.x) < minDistanceX || minDistanceX == 0) {
                    minDistanceX = Math.abs(p.x - dis.x);
                }
            }
        }
        int diff_x = 0;
        if (minX > 10) {
            diff_x = minX - 10;
        }
        double scalX = 1, scalY = 1;
        if (minDistanceX > 40) {
            scalX = minDistanceX * 1f / 40;
        }
        int height = getHeight();
        if (maxY > height && height != 0) {
            scalY = (height * 1f - 30) / maxY;
        }
        int index = 0;
        for (Point p : pointTable) {
            PointView view = new PointView(getContext());
            view.setPosition((int) ((p.x - diff_x) / scalX) + 12, (int) (height - (p.y * scalY)));
            view.setTouchListener(index, this);
            index++;
            addView(view);
            pointViewList.add(view);
        }
        invalidate();
    }

    private int touchIndex = -1;

    @Override
    public void onTouch(int index, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                break;
        }
        invalidate();
    }
}