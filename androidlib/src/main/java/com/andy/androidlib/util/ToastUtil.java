package com.andy.androidlib.util;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

/**
 * Toast {@link android.widget.Toast} 工具类
 *
 * Created by andy on 17-11-10.
 */

public class ToastUtil {
    private final String TAG = getClass().getSimpleName();
    private static ToastUtil mToastUtil = null;

    private ToastUtil(){}

    public static ToastUtil getInstance() {
        if(mToastUtil == null){
            mToastUtil = new ToastUtil();
        }
        return mToastUtil;
    }

    private Toast mToast = null;
    public void init(@NotNull Context context){
        Log.d(TAG, "init ToastUtil start");
        mToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER,0,0);
        Log.d(TAG, "init ToastUtil success");
    }

    public void lengthShow(@NotNull String msg){
        if(mToast == null){
            Log.d(TAG, "not init ToastUtil");
            return;
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(msg);
        mToast.show();
    }

    public void shortShow(@NotNull String msg){
        if(mToast == null){
            Log.d(TAG, "not init ToastUtil");
            return;
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(msg);
        mToast.show();
    }

}
