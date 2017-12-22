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
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.GRAY);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.GRAY);
        init();
    }
    private X_YView xYView;
    private void init(){
        xYView = new X_YView(getContext());
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


        if (touchIndex != -1) {
            PointView v = (PointView) getChildAt(touchIndex+1);
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

    public void setPadding(int padding){
        xYView.setPadding(padding);
    }

    //依据X轴从小到大排序
    private ArrayList<Point> sort(ArrayList<Point> points) {
        ArrayList<Point> tmp = new ArrayList<>();
        tmp.addAll(points);
        int len = tmp.size();
        for(int i = 0; i<len; i++){
            for(int j = i+1; j < len; j++){
                Point pre = tmp.get(i);
                Point suf = tmp.get(j);
                if(pre.x > suf.x){
                    tmp.set(i, suf);
                    tmp.set(j,pre);
                }
            }
        }
        return tmp;
    }

    private ArrayList<PointView> pointViewList = new ArrayList<>();

    private void analysis() {
        int minX =0, minY = 0, maxX=0, maxY=0;
        int flag = 0;
        for (Point p : pointTable) {
            if(flag == 0){
                minX = maxX = p.x;
                minY = maxY = p.y;
                flag++;
            }

            if(maxX < p.x){
                maxX = p.x;
            }

            if (maxY < p.y) {
                maxY = p.y;
            }

            if(minX > p.x){
                minX = p.x;
            }

            if(minY > p.y){
                minY = p.y;
            }
        }
        //判断XY轴类型
        if((maxX < 0 && maxY < 0)||(minX >0 && minY > 0)|| (maxX <0 && minY > 0)||(minX > 0 && maxY < 0)){
            xYView.setStyle(X_YView.Style.SX_SY);
        }else if((minX < 0 && maxX > 0 && minY > 0)||(minX < 0 && maxX > 0 && maxY < 0)){
            xYView.setStyle(X_YView.Style.X_SY);
        }else if ((minY < 0 && maxY > 0 && minX > 0)||(minY < 0 && maxY > 0 && maxX <0)){
            xYView.setStyle(X_YView.Style.SX_Y);
        }else{
            xYView.setStyle(X_YView.Style.X_Y);
        }
        addView(xYView);

        double scalX = 1, scalY = 1;
        int width = getWidth()- 2*xYView.padding;
        if ((maxX - minX) > width && width != 0) {
            scalX = (width * 1f - 30) / (maxX - minX);
        }

        int height = getHeight() - 2*xYView.padding;
        if ((maxY - minY) > height && height != 0) {
            scalY = (height * 1f - 30) / (maxY - minY);
        }

        int index = 0;
        for (Point p : pointTable) {
            PointView view = new PointView(getContext());
            view.setPosition((int) ((p.x-minX) *scalX)+xYView.padding, (int) (height - ((p.y+minY) * scalY)) -xYView.padding);
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