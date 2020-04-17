package com.ylzb.fastpay;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.ylzb.fastpay.pay.PayManager;
import com.ylzb.fastpay.util.Base64Util;
import com.ylzb.fastpay.util.CLog;
import com.ylzb.fastpay.util.JsonUtil;

public class JsCallAction {
    private final String TAG = JsCallAction.class.getSimpleName();

    @JavascriptInterface
    public void zb_pay(String param) {
        CLog.e(TAG,param);
        JsParamInfo paramInfo = (JsParamInfo) JsonUtil.parsData(param,JsParamInfo.class);
        if (paramInfo != null) {
            String type = paramInfo.type;
            String payParam = Base64Util.setDecrypt(paramInfo.pay_info);
            if (JsParamInfo.PAY_TYPE_ZFB.equals(type)){
                PayManager.getInstance().aliPay((Activity) PayManager.getInstance().getContext(),payParam);
            }else if (JsParamInfo.PAY_TYPE_WX.equals(type)){

            }
        }
    }

}
