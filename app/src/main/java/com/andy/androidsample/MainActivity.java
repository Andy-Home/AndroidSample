package com.andy.androidsample;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.andy.androidlib.view.chart.LineView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private LineView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.line);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.setPadding(10);

                ArrayList<Point> points = new ArrayList<>();
                points.add(new Point(180, 700));
                points.add(new Point(220, 110));
                points.add(new Point(260, 130));
                points.add(new Point(340, 130));
                points.add(new Point(100, 900));
                points.add(new Point(140, 150));
                points.add(new Point(300, 10));
                view.setPoints(points);

                view.setAnimation(6000);
                view.start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }


}
