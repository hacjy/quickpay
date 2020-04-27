package com.ylzb.fastpay_demo;

import android.app.Application;
import android.content.Context;

import com.ylzb.fastpay.pay.PayManager;
import com.ylzb.fastpay.pay.PayResultFromServer;
import com.ylzb.fastpay.util.CLog;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance(){
        if (instance == null){
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initPayResult();
    }

    private void initPayResult(){
        PayManager.getInstance().setPayResult(new PayResultFromServer() {
            @Override
            public void result(Context context,String param) {
                CLog.e("服务端支付结果回调",param);
            }
        });
    }

}
