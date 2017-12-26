package com.andy.androidlib.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * 根据屏幕分辨率获取1dp中的像素大小
 * <p>
 * Created by Andy on 2017/12/23.
 */

public class DPValue {
    private static DPValue dpValue;

    private DPValue() {
    }

    private void init() {

    }

    public static DPValue getInstance() {
        if (dpValue == null) {
            dpValue = new DPValue();
        }
        return dpValue;
    }

    private static float scale = 1f;

    public void init(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
        Log.d("DPValue", "scale:" + scale);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }
}
