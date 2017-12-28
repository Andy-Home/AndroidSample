package com.andy.androidlib.view.chart;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.andy.androidlib.R;
import com.andy.androidlib.view.Colors;
import com.andy.androidlib.view.DPValue;

import java.util.ArrayList;

public class LineView extends ViewGroup implements PointView.onTouchListener {
    final String TAG = getClass().getSimpleName();
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
    private Paint mPaint, mTextPaint, mDashPaint;

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

        mDashPaint = new Paint();
        mDashPaint.setStrokeWidth(DPValue.dp2px(1));
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int radius = PointView.radius;
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

    private int currentX = 0;
    private int viewIndex = -1;

    @Override
    protected void onDraw(final Canvas canvas) {
        //点击PointView后的显示
        if (touchIndex != -1) {
            PointView v = (PointView) getChildAt(touchIndex + 1);

            canvas.drawLine(v.p_x, v.p_y, xYView.origin.x, v.p_y, mDashPaint); //Y
            if (v.p_y > DPValue.dp2px(15)) {
                canvas.drawText(v.y, xYView.origin.x, v.p_y, mTextPaint);
            } else {
                canvas.drawText(v.y, xYView.origin.x, v.p_y + DPValue.dp2px(12), mTextPaint);
            }

            canvas.drawLine(v.p_x, v.p_y, v.p_x, xYView.origin.y, mDashPaint);//X
            if (v.p_x < getWidth() - DPValue.dp2px(15)) {
                canvas.drawText(v.x, v.p_x, xYView.origin.y, mTextPaint);
            } else {
                canvas.drawText(v.x, v.p_x - DPValue.dp2px(15), xYView.origin.y, mTextPaint);
            }
        }

        int endIndex = indexAnimation();

        if (endIndex > viewIndex) {

            for (int i = viewIndex + 1; i <= endIndex; i++) {
                addView(pointViewList.get(i));
            }
            viewIndex = endIndex;

        }

        //连线
        for (int i = 0; i < endIndex; i++) {
            PointView start = pointViewList.get(i);
            PointView end = pointViewList.get(i + 1);
            canvas.drawLine(start.p_x, start.p_y, end.p_x, end.p_y, mPaint);
        }
        //画尾线
        if ((pointViewList.get(endIndex).p_x < currentX) && (endIndex != pointViewList.size() - 1)) {
            PointView start = pointViewList.get(endIndex);
            PointView next = pointViewList.get(endIndex + 1);
            float gradient = (next.p_y - start.p_y) * 1f / (next.p_x - start.p_x);
            canvas.drawLine(start.p_x, start.p_y, currentX, (currentX - start.p_x) * gradient + start.p_y, mPaint);
        }
    }

    /**
     * 根据 currentX 的值采用二分查找法从 pointViewList获取动画绘画位置
     */
    private int indexAnimation() {
        int start = 0;
        int index = 0;
        int end = pointViewList.size() - 1;
        int middle;
        while (start <= end) {
            middle = (start + end) / 2;
            if (middle == pointViewList.size() - 1 || middle == 0) {
                return middle;
            }

            if (pointViewList.get(middle).p_x > currentX &&
                    !(pointViewList.get(middle - 1).p_x < currentX)) {
                end = middle - 1;
            } else if (pointViewList.get(middle).p_x < currentX &&
                    !(pointViewList.get(middle + 1).p_x > currentX)) {
                start = middle + 1;
            } else if (pointViewList.get(middle).p_x > currentX &&
                    pointViewList.get(middle - 1).p_x < currentX) {
                index = middle - 1;
                break;
            } else if (pointViewList.get(middle).p_x < currentX &&
                    pointViewList.get(middle + 1).p_x > currentX) {
                index = middle;
                break;
            } else if (pointViewList.get(middle).p_x == currentX) {
                index = middle;
                break;
            }
        }
        return index;
    }

    private ArrayList<Point> pointTable;

    public void setPoints(ArrayList<Point> points) {
        if (points == null) {
            points = new ArrayList<>();
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

    //依据X轴从小到大排序(冒泡排序)
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
            view.setTouchListener(index, this);
            view.setMessage(String.valueOf(p.x), String.valueOf(p.y));
            index++;
            pointViewList.add(view);
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "按下");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (animator != null && animator.isRunning()) {
                        animator.cancel();
                    }
                } else {
                    if (animatorThread != null && animatorThread.isAlive()) {
                        animatorThread.interrupt();
                    }
                }
                currentX = pointViewList.get(pointViewList.size() - 1).p_x;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private int timeLength = 0;
    public void setAnimation(final int timeLength) {
        this.timeLength = timeLength;
    }

    private Thread animatorThread;
    private ValueAnimator animator;
    public void start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            animator = ValueAnimator.ofInt(pointViewList.get(0).p_x, pointViewList.get(pointViewList.size() - 1).p_x);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentX = (Integer) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.setDuration(timeLength);
            animator.start();
        } else {
            animatorThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    currentX = pointViewList.get(0).p_x;
                    int maxX = pointViewList.get(pointViewList.size() - 1).p_x;
                    int period = timeLength / (maxX - currentX);
                    while (currentX <= maxX) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                invalidate();
                            }
                        });
                        try {
                            Thread.currentThread().sleep(period);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        currentX += 1;
                    }

                }
            });
            animatorThread.start();
        }
    }
}