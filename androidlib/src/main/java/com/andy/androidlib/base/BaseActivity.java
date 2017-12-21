package com.andy.androidlib.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity 基础型
 *
 * Created by andy on 17-11-11.
 */

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    public abstract void initView();
    public void initData(){}
}
