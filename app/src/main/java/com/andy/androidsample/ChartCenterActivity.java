package com.andy.androidsample;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.andy.androidlib.base.BaseActivity;

public class ChartCenterActivity extends BaseActivity implements View.OnClickListener {
    private Button pie, line;

    @Override
    public void initView() {
        setContentView(R.layout.activity_chart_center);

        pie = findViewById(R.id.pie);
        line = findViewById(R.id.line);

        pie.setOnClickListener(this);
        line.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.pie:
                intent = new Intent(this, PieActivity.class);
                startActivity(intent);
                break;
            case R.id.line:
                intent = new Intent(this, LineActivity.class);
                startActivity(intent);
                break;
        }

    }
}
