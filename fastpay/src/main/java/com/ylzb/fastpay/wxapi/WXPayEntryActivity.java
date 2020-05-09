package com.ylzb.fastpay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ylzb.fastpay.Event;
import com.ylzb.fastpay.R;
import com.ylzb.fastpay.pay.PayManager;
import com.ylzb.fastpay.util.CLog;
import com.ylzb.fastpay.util.JsonUtil;

/**
 * 微信回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastpay_pay_result);

        api = WXAPIFactory.createWXAPI(this, PayManager.getInstance().getWxAppId());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        int code = resp.errCode;
        String transaction = resp.transaction;
        String errStr = resp.errStr;
        CLog.e("wx-payResult", JsonUtil.objectToString(resp));
        int resultCode = -1;
        if (code == 0) {
            //显示充值成功的页面和需要的操作
            resultCode = Event.PayResultEvent.PAY_RESULT_SUCCESS;
        }
        if (code == -1) {
            //错误   可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            resultCode = Event.PayResultEvent.PAY_RESULT_FAILED;
        }

        if (code == -2) {
            //用户取消
            resultCode = Event.PayResultEvent.PAY_RESULT_CANCEL;
        }
        PayManager.getInstance().callPayResult(resultCode);
        finish();
    }
}