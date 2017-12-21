package com.andy.androidlib.view;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressView extends ProgressDialog {
    public ProgressView(Context context, int theme) {
        super(context, theme);
    }

    public ProgressView(Context context) {
        super(context);
    }
}
