package com.andy.androidsample.activity.chart;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andy.androidlib.base.BaseActivity;
import com.andy.androidsample.R;
import com.andy.androidsample.entity.CenterActivityItem;

import java.util.ArrayList;
import java.util.List;

public class ChartCenterActivity extends BaseActivity {
    private RecyclerView vRecyclerView;
    private LinearLayoutManager vLinearLayoutManager;
    private ChartAdapter vAdapter;

    @Override
    public void initData() {
        super.initData();
        vLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        List<CenterActivityItem> data = new ArrayList<>();
        CenterActivityItem item1 = new CenterActivityItem(getResources().getString(R.string.pie_view), PieActivity.class);
        CenterActivityItem item2 = new CenterActivityItem(getResources().getString(R.string.line_view), LineActivity.class);
        data.add(item1);
        data.add(item2);

        vAdapter = new ChartAdapter(data);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_chart_center);

        vRecyclerView = findViewById(R.id.list);
        vRecyclerView.setLayoutManager(vLinearLayoutManager);
        vRecyclerView.setAdapter(vAdapter);
    }
}
