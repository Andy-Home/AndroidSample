package com.andy.androidsample;

import android.os.Build;
import android.view.ViewTreeObserver;

import com.andy.androidlib.base.BaseActivity;
import com.andy.androidlib.entity.PieContent;
import com.andy.androidlib.view.chart.PieView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 17-12-29.
 */

public class PieActivity extends BaseActivity {
    private PieView pieView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_chart_pie);
        pieView = findViewById(R.id.pie);
        pieView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                List<PieContent> data = new ArrayList<>();
                PieContent content1 = new PieContent();
                content1.setPercent(0.135f);
                content1.setContent("饮食消费");

                PieContent content2 = new PieContent();
                content2.setPercent(0.316f);
                content2.setContent("住宿");

                PieContent content3 = new PieContent();
                content3.setPercent(0.26f);
                content3.setContent("交通");

                PieContent content4 = new PieContent();
                content4.setPercent(0.26f);
                content4.setContent("交通");

                data.add(content1);
                data.add(content2);
                data.add(content3);
                data.add(content4);

                try {
                    pieView.setData(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pieView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    pieView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }
}
