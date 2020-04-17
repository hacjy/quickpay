package com.ylzb.fastpay.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ylzb.fastpay.Event;
import com.ylzb.fastpay.PayConfig;
import com.ylzb.fastpay.util.CLog;
import com.ylzb.fastpay.util.JsonUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 支付类
 */

public class PayManager {
    private final String TAG = PayManager.class.getSimpleName();

    private IWXAPI msgApi = null;
    private PayConfig payConfig;
    private Context context;
    private IPayListener payListener;

    private static PayManager mInstance = null;

    public static PayManager getInstance() {
        if (mInstance == null) {
            mInstance = new PayManager();
        }
        return mInstance;
    }

    /**
     * 初始化配置
     * @param payConfig
     */
    public void initPayConfig(PayConfig payConfig){
        this.payConfig = payConfig;
        if (payConfig != null) {
            context = payConfig.context;
        }
    }

    public void setPayListener(IPayListener payListener) {
        this.payListener = payListener;
    }

    private PayManager() {

    }

    public Context getContext() {
        return context;
    }

    public String getWxAppId(){
        if (payConfig != null)
            return payConfig.wxAppId;
        return "";
    }

    private boolean hasContext(){
        if (context == null)
            return false;
        return true;
    }

    public void wxPay(PayModel.ReturnDataBean data) {
        if (!hasContext()){
            CLog.e(TAG,"context is null");
            return;
        }
        if (msgApi == null) {
            msgApi = WXAPIFactory.createWXAPI(context, payConfig.wxAppId);
            msgApi.registerApp(payConfig.wxAppId);// 将该app注册到微信
        }
        PayReq req = new PayReq();
        if (!msgApi.isWXAppInstalled()) {
            Toast.makeText(context, "手机中没有安装微信客户端!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data != null) {
            req.appId = data.getAppid();
            req.partnerId = data.getPartnerid();
            req.prepayId = data.getPrepayid();
            req.nonceStr = data.getNoncestr();
            req.timeStamp = data.getTimestamp();
            req.packageValue = data.getPackageX();
            req.sign = data.getSign();
            msgApi.sendReq(req);
        }
    }

    public void aliPay(Activity activity, final String orderInfo) {
        if (!hasContext()){
            CLog.e(TAG,"context is null");
            return;
        }
        final PayTask alipay = new PayTask(activity);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

                Map<String, String> result = alipay.payV2(orderInfo, true);
                CLog.i("alipayV2", result.toString());
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    CLog.e("ali-payResult:", JsonUtil.objectToString(payResult));
                    String resultStatus = payResult.getResultStatus();
                    int resultCode = -1;
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        resultCode = Event.PayResultEvent.PAY_RESULT_SUCCESS;
                    }else if (TextUtils.equals(resultStatus, "6001")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        ToastUtil.showToast(DJHealthApplication.getInstance(),"取消支付");
                        resultCode = Event.PayResultEvent.PAY_RESULT_CANCEL;
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        resultCode = Event.PayResultEvent.PAY_RESULT_FAILED;
                    }
                    callPayResult(resultCode);
                    break;
            }
        }
    };


    public void callPayResult(int resultCode){
        if (payListener != null){
            if (resultCode == Event.PayResultEvent.PAY_RESULT_SUCCESS) {
                payListener.onSuccess();
            }else if (resultCode == Event.PayResultEvent.PAY_RESULT_FAILED) {
                payListener.onFail();
            }else if (resultCode == Event.PayResultEvent.PAY_RESULT_CANCEL) {
                payListener.onCancel();
            }
        }
    }

    /**
     * 获取sign签名
     *
     * @return
     */
    private String genPayReq(PayModel.ReturnDataBean model,String appkey) {
        // 获取参数的值
        PayReq request = new PayReq();
        request.appId = model.getAppid();
        request.partnerId = model.getPartnerid();
        request.prepayId = model.getPrepayid();
        request.packageValue = model.getPackageX();
        request.nonceStr = model.getNoncestr();
        request.timeStamp = model.getTimestamp();

        // 把参数的值传进去SortedMap集合里面
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
//  {appid=wx34df375d7dae8c90, noncestr=3BF34EF2CA4A462DB8D4EA48E785CDC3,
//    package=Sign=WXPay, partnerid=1349967601,
//    prepayid=wx2016070910354542c7155d4e0846850809, timestamp=1468031760}
        parameters.put("appid", request.appId);
        parameters.put("noncestr", request.nonceStr);
        parameters.put("package", request.packageValue);
        parameters.put("partnerid", request.partnerId);
        parameters.put("prepayid", request.prepayId);
        parameters.put("timestamp", request.timeStamp);

        String characterEncoding = "UTF-8";
        String mySign = createSign(characterEncoding, parameters,appkey);
        System.out.println("我的签名是：" + mySign);
        return mySign;
    }

    public static long getTimestamp(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding,
                                    SortedMap<Object, Object> parameters,String appkey) {

        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + appkey); //KEY是商户秘钥
        String sign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        return sign; // D3A5D13E7838E1D453F4F2EA526C4766
        // D3A5D13E7838E1D453F4F2EA526C4766
    }
}
