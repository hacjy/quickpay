package com.ylzb.fastpay;

import android.content.Context;

public class PayConfig {
    public Context context;
    public String wxAppId;

    public PayConfig() {
    }

    public PayConfig(Context context, String wxAppId) {
        this.context = context;
        this.wxAppId = wxAppId;
    }
}
