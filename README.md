# fastpay
[![](https://jitpack.io/v/hacjy/fastpay.svg)](https://jitpack.io/#hacjy/fastpay)
快速支付，支持微信支付宝

使用步骤：
在Application初始化：    
  
1、设置微信APPID：  
```
PayManager.getInstance().initPayConfig(new PayConfig(MainActivity.this,""));  
```
  
2、支付结果回调：  
```
PayManager.getInstance().setPayResult(new PayResultFromServer() {  
            @Override  
            public void result(Context context,String param) {  
                CLog.e("服务端支付结果回调",param);  
            }  
        }); 
```
