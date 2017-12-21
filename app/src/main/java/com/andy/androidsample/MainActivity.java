package com.andy.androidsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.andy.androidlib.view.chart.LineView;

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
                view.setPoints(null);
            }
        });
    }


}
