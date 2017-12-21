package com.andy.androidlib.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 创建类似 ProgressDialog {@link android.app.ProgressDialog} 功能的工具类
 *
 * Created by andy on 17-11-10.
 */

public class ProgressUtil {
    private static ProgressUtil mProgressUtil = null;

    private ProgressUtil(){}

    public static ProgressUtil getInstance() {
        if (mProgressUtil == null) {
            mProgressUtil = new ProgressUtil();
        }
        return mProgressUtil;
    }

    private ProgressDialog mProgressDialog = null;

    public void showProgressDialog(Context context){

    }
}
