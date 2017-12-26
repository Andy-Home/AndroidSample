package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.andy.androidlib.R;
import com.andy.androidlib.view.Colors;
import com.andy.androidlib.view.DPValue;

import java.util.ArrayList;

public class LineView extends ViewGroup implements PointView.onTouchListener {
    public LineView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.WHITE);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);
        init();
    }
    private X_YView xYView;
    private Paint mPaint, mTextPaint;

    private void init() {
        xYView = new X_YView(getContext());
        mPaint = new Paint();
        mPaint.setStrokeWidth(DPValue.dp2px(2));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Colors.LightSeaGreen);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Colors.Navy);
        mTextPaint.setTextSize(DPValue.dp2px(12));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int radius = DPValue.dp2px(6);
        for (int index = 0; index < getChildCount(); index++) {
            View v = getChildAt(index);
            if (v instanceof PointView) {
                int x = ((PointView) v).p_x;
                int y = ((PointView) v).p_y;
                v.layout(x - radius, y - radius, x + radius, y + radius);
            } else {
                v.layout(l, t, r, b);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (touchIndex != -1) {
            PointView v = (PointView) getChildAt(touchIndex + 1);
            canvas.drawText(v.msg, v.p_x + DPValue.dp2px(14), v.p_y - DPValue.dp2px(14), mTextPaint);
        }

        for (int i = 0; i < pointViewList.size() - 1; i++) {
            PointView start = pointViewList.get(i);
            PointView end = pointViewList.get(i + 1);
            canvas.drawLine(start.p_x, start.p_y, end.p_x, end.p_y, mPaint);
        }
    }

    private ArrayList<Point> pointTable;

    public void setPoints(ArrayList<Point> points) {
        if (points == null) {
            points = new ArrayList<>();
            points.add(new Point(180, 700));
            points.add(new Point(220, 110));
            points.add(new Point(260, 130));
            points.add(new Point(340, 130));
            points.add(new Point(100, 900));
            points.add(new Point(140, 150));
            points.add(new Point(300, 10));
        }
        pointTable = sort(points);
        analysis();
    }

    /**
     * 设置View与边缘的间距
     *
     * @param padding 单位 dp
     */
    public void setPadding(int padding) {
        xYView.setPadding(DPValue.dp2px(padding));
    }

    //依据X轴从小到大排序
    private ArrayList<Point> sort(ArrayList<Point> points) {
        ArrayList<Point> tmp = new ArrayList<>();
        tmp.addAll(points);
        int len = tmp.size();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                Point pre = tmp.get(i);
                Point suf = tmp.get(j);
                if (pre.x > suf.x) {
                    tmp.set(i, suf);
                    tmp.set(j, pre);
                }
            }
        }
        return tmp;
    }

    private ArrayList<PointView> pointViewList = new ArrayList<>();

    private void analysis() {
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        int flag = 0;
        for (Point p : pointTable) {
            if (flag == 0) {
                minX = maxX = p.x;
                minY = maxY = p.y;
                flag++;
            }

            if (maxX < p.x) {
                maxX = p.x;
            }

            if (maxY < p.y) {
                maxY = p.y;
            }

            if (minX > p.x) {
                minX = p.x;
            }

            if (minY > p.y) {
                minY = p.y;
            }
        }

        double scalX = 1, scalY = 1;
        int width = getWidth() - (2 * xYView.padding);
        if (Math.abs(maxX - minX) > width && width != 0) {
            scalX = (width * 1f) / Math.abs(maxX - minX);
        }

        int height = getHeight() - (2 * xYView.padding);
        if (Math.abs(maxY - minY) > height && height != 0) {
            scalY = (height * 1f) / Math.abs(maxY - minY);
        }
        //XY轴原点
        Point point = new Point();

        //判断XY轴类型
        if ((maxX < 0 && maxY < 0) || (minX > 0 && minY > 0) || (maxX < 0 && minY > 0) || (minX > 0 && maxY < 0)) {
            point.x = xYView.padding;
            point.y = getHeight() - xYView.padding;
            xYView.setStyle(X_YView.Style.SX_SY, point);
        } else if ((minX < 0 && maxX > 0 && minY > 0) || (minX < 0 && maxX > 0 && maxY < 0)) {
            point.x = (int) (scalX * Math.abs(minX));
            point.y = getHeight() - xYView.padding;
            xYView.setStyle(X_YView.Style.X_SY, point);
        } else if ((minY < 0 && maxY > 0 && minX > 0) || (minY < 0 && maxY > 0 && maxX < 0)) {
            point.x = xYView.padding;
            point.y = (int) (scalY * Math.abs(maxY));
            xYView.setStyle(X_YView.Style.SX_Y, point);
        } else {
            point.x = (int) (scalX * Math.abs(minX));
            point.y = (int) (scalY * Math.abs(maxY));
            xYView.setStyle(X_YView.Style.X_Y, point);
        }
        addView(xYView);

        int index = 0;
        int min_X = Math.abs(minX) > Math.abs(maxX) ? maxX : minX;
        int min_Y = Math.abs(minY) > Math.abs(maxY) ? maxY : minY;
        for (Point p : pointTable) {
            PointView view = new PointView(getContext());
            if (xYView.style == X_YView.Style.SX_SY) {
                view.setPosition((int) ((Math.abs(p.x - min_X)) * scalX) + point.x,
                        point.y - (int) ((Math.abs(p.y + min_Y)) * scalY));
            } else if (xYView.style == X_YView.Style.X_SY) {
                view.setPosition((int) (p.x * scalX) + point.x,
                        point.y - (int) ((Math.abs(p.y + min_Y)) * scalY));
            } else if (xYView.style == X_YView.Style.SX_Y) {
                view.setPosition((int) ((Math.abs(p.x - min_X)) * scalX) + point.x,
                        point.y - (int) (p.y * scalY));
            } else if (xYView.style == X_YView.Style.X_Y) {
                view.setPosition((int) (p.x * scalX) + point.x, point.y - (int) (p.y * scalY));
            }
            //view.setPosition((int) (p.x * scalX) + point.x, point.y - (int) (p.y * scalY));
            view.setTouchListener(index, this);
            view.setMessage("x:" + p.x + " y:" + p.y);
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