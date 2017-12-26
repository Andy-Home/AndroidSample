package com.andy.androidlib.base;

import android.app.Application;

import com.andy.androidlib.view.DPValue;

/**
 * Created by Administrator on 2017/12/23.
 */

public abstract class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DPValue.getInstance().init(this);
    }
}
