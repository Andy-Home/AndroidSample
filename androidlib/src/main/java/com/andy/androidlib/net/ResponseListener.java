package com.andy.androidlib.net;

public interface ResponseListener {

    void onSuccess(String result);

    void onFailure(String msg);
}
