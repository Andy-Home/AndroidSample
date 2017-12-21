package com.andy.androidlib.util;

import android.content.SharedPreferences;

/**
 * {@link android.content.SharedPreferences} 工具类
 *
 * Created by andy on 17-11-17.
 */

public class SharedPreferencesUtil {
    private SharedPreferences mSharedPreferences;

    public SharedPreferencesUtil(SharedPreferences preferences){
        mSharedPreferences = preferences;
    }

    public int getInt(String var1, int var2){
        return mSharedPreferences.getInt(var1, var2);
    }


}
