package com.andy.androidlib.view.chart;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.andy.androidlib.view.DPValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 矩状图
 * <p>
 * Created by andy on 18-1-17.
 */

public class RectView extends View {
    public RectView(Context context) {
        super(context);
        init();
    }

    public RectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        blankWidth = DPValue.dp2px(2);
    }

    private List<Point> mChartData;

    /**
     * 设置图表数据
     *
     * @param data {@link Point} 通过设置x轴来区分先后
     */
    public void setChartData(ArrayList<Point> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        if (mDefaultSort) {
            mChartData = sort(data);
        } else {
            mChartData = data;
        }
        analysis();
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

    private float rectWidth = 0;
    private float blankWidth = 0;

    private void analysis() {
        int screenWidth = getWidth();
        int size = mChartData.size();
        rectWidth = (screenWidth - ((size + 1) * blankWidth)) * 1f / size;

        int maxValue = 0;
        for (int i = 0; i < size; i++) {
            if (maxValue < mChartData.get(i).y) {
                maxValue = mChartData.get(i).y;
            }
        }
    }

    private boolean mDefaultSort = true;

    /**
     * 启用默认排序,默认启用
     *
     * @param enable true为启用排序,false为禁用排序
     */
    public void isEnableSort(boolean enable) {
        mDefaultSort = enable;
    }
}
