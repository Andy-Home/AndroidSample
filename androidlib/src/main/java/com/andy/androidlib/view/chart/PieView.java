package com.andy.androidlib.view.chart;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.andy.androidlib.R;
import com.andy.androidlib.entity.PieContent;
import com.andy.androidlib.view.DPValue;

import java.text.DecimalFormat;
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
    private Paint mPiePaint, mTextPaint;
    private Paint mOnePiePaint, mOneTextPaint;

    private void init() {
        setBackgroundColor(Color.WHITE);
        mPiePaint = new Paint();
        mPiePaint.setAntiAlias(true);
        mPiePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(DPValue.dp2px(8));

        mOnePiePaint = new Paint();
        mOnePiePaint.setAntiAlias(true);
        mOnePiePaint.setStyle(Paint.Style.FILL);

        mOneTextPaint = new Paint();
        mOneTextPaint.setAntiAlias(true);
        mOneTextPaint.setColor(Color.BLACK);
        mOneTextPaint.setTextSize(DPValue.dp2px(12));
        mOneTextPaint.setTextAlign(Paint.Align.CENTER);

        mFormat = new DecimalFormat("##0.00");
    }

    private DecimalFormat mFormat;
    private ArrayList<SectorInfo> sectorInfos = new ArrayList<>();

    private int alpha = 255;

    @Override
    protected void onDraw(Canvas canvas) {
        if (data != null && data.size() != 0) {
            SectorInfo info = null;
            if (clickIndex != -1) {
                info = sectorInfos.get(clickIndex);
            }

            float start = 0, percent;
            int flag = 0, contentStartY = contentY;
            sectorInfos.clear();
            for (PieContent content : data) {
                percent = content.getPercent() * 360;
                mPiePaint.setColor(colors[flag]);
                mPiePaint.setAlpha(alpha);
                mTextPaint.setAlpha(alpha);
                canvas.drawRect(contentX, contentStartY, contentX + DPValue.dp2px(6), contentStartY + DPValue.dp2px(6), mPiePaint);
                if (info != null && info.content.equals(content)) {
                    mOnePiePaint.setColor(colors[flag]);
                    canvas.drawArc(scalRectF, currentStart, info.radian, true, mOnePiePaint);
                    canvas.drawText(mFormat.format(content.getPercent() * 100) + "%", scalRectF.centerX(), scalRectF.centerY() - (scalRectF.height() / 4), mOneTextPaint);
                    canvas.drawText(content.getContent(), scalRectF.centerX(), scalRectF.centerY() + DPValue.dp2px(12), mOneTextPaint);
                    mPiePaint.setColor(Color.WHITE);
                    mPiePaint.setAlpha(alpha);
                    canvas.drawArc(rectF, start, percent, true, mPiePaint);

                } else {
                    canvas.drawArc(rectF, start, percent, true, mPiePaint);
                }
                canvas.drawText(mFormat.format(content.getPercent() * 100) + "%", contentX + DPValue.dp2px(10), contentStartY + DPValue.dp2px(6), mTextPaint);
                canvas.drawText(content.getContent(), contentX + DPValue.dp2px(40), contentStartY + DPValue.dp2px(6), mTextPaint);

                sectorInfos.add(new SectorInfo(start, start + percent, percent, content, colors[flag]));
                flag++;
                start += percent;
                contentStartY += 2 * DPValue.dp2px(6);
            }

        }
    }

    int contentX = 0, contentY = 0; //文字内容的最初起始位置

    int radius = 0;     //饼状图半径

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
            rectF = new RectF(minX, y, 5 * minX, getHeight() - y);

            radius = 2 * minX;
            Log.d(TAG, "centerX:" + rectF.centerX() + " centerY:" + rectF.centerY());
            contentX = getWidth() - 2 * minX;
            contentY = y - minX / 2;

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

    private int clickIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (clickIndex == -1) {
                    Log.d(TAG, "x:" + event.getX() + " y:" + event.getY());
                    int index = clickView(event.getX(), event.getY());
                    if (index != -1) {
                        clickIndex = index;
                        clickAnimation(index);
                    }
                } else {
                    clickIndex = -1;
                    alpha = 255;
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int clickView(float x, float y) {
        float a = x - rectF.centerX();
        float b = y - rectF.centerY();
        double c = Math.sqrt(a * a + b * b);
        if (c <= radius) {
            double radian = 0;
            if (x > rectF.centerX() && y < rectF.centerY()) {
                radian = 360 - Math.toDegrees(Math.atan((rectF.centerY() - y) / (x - rectF.centerX())));
            } else if (x > rectF.centerX() && y > rectF.centerY()) {
                radian = Math.toDegrees(Math.atan((y - rectF.centerY()) / (x - rectF.centerX())));
            } else if (x < rectF.centerX() && y > rectF.centerY()) {
                radian = 180 - Math.toDegrees(Math.atan((y - rectF.centerY()) / (rectF.centerX() - x)));
            } else if (x < rectF.centerX() && y < rectF.centerY()) {
                radian = 180 + Math.toDegrees(Math.atan((rectF.centerY() - y) / (rectF.centerX() - x)));
            }

            for (int i = 0; i < sectorInfos.size(); i++) {
                SectorInfo info = sectorInfos.get(i);
                if (info.startRadian <= radian && info.endRadian > radian) {
                    return i;
                }
            }
        }
        return -1;
    }

    private float currentStart;
    private Thread animatorThread;
    private RectF scalRectF = rectF;
    private final long timeLength = 800;

    private void clickAnimation(int index) {
        final SectorInfo info = sectorInfos.get(index);
        float start = info.startRadian;
        float end = start + (270 - (info.startRadian + info.endRadian) / 2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator animator = ValueAnimator.ofFloat(start, start + 450, end + 360);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentStart = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });

            ValueAnimator animator1 = ValueAnimator.ofFloat(8, 6);
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (info.radian <= 180) {
                        float minX = getWidth() * 1f / (Float) animation.getAnimatedValue();
                        float y = (getHeight() - (minX * 4)) * 1f / 2;
                        Log.d(TAG, "x:" + minX + " y:" + y);
                        scalRectF = new RectF(minX, y, 5 * minX, getHeight() - y);
                    }
                }
            });


            ValueAnimator animator2 = ValueAnimator.ofInt(255, 0);
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    alpha = (Integer) valueAnimator.getAnimatedValue();
                }
            });

            AnimatorSet animationSet = new AnimatorSet();
            animationSet.setDuration(timeLength);
            animationSet.play(animator).with(animator1).with(animator2);
            animationSet.start();
        } else {
            /*animatorThread = new Thread(new Runnable() {
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
            animatorThread.start();*/
        }
    }

    class SectorInfo {
        float startRadian, endRadian, radian;
        PieContent content;
        int color;

        public SectorInfo(float start, float end, float radian, PieContent content, int color) {
            this.startRadian = start;
            this.endRadian = end;
            this.radian = radian;
            this.content = content;
            this.color = color;
        }
    }
}
