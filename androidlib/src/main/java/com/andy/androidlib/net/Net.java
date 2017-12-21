package com.andy.androidlib.net;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络模块
 *
 * Created by andy on 17-11-14.
 */

public class Net {
    private static final int TIMEOUT = 5*1000;
    private Handler mHandler;
    public Net(){
        mHandler = new Handler(Looper.getMainLooper());
    }
    public void upload(final URL url, final String content,
                       final ResponseListener listener) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                byte[] sendData = content.getBytes();
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(TIMEOUT);
                    // 如果通过post提交数据，必须设置允许对外输出数据
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "text/xml");
                    conn.setRequestProperty("Charset", "utf-8");
                    conn.setRequestProperty("Content-Length",
                            String.valueOf(sendData.length));
                    OutputStream outStream;
                    outStream = conn.getOutputStream();
                    outStream.write(sendData);
                    outStream.flush();
                    outStream.close();
                    final int response = conn.getResponseCode();
                    if (response == 200) {
                        // 获得服务器响应的数据
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                        // 数据
                        String retData;
                        String responseData ="";
                        while ((retData = in.readLine()) != null) {
                            responseData += retData;
                        }
                        in.close();
                        final String finalResponseData = responseData;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onSuccess(finalResponseData);
                            }
                        });
                        return;
                    }else{
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFailure("应答结果："+response);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure("网络错误");
                        }
                    });
                }

            }
        }).start();
    }
}
