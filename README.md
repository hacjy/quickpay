# fastpay
快速支付，支持微信支付宝

使用步骤：  
(一）在Application初始化：    
  
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
  
（二）在主工程的app build.gradle中设置微信appid
```
manifestPlaceholders = [
  wx_appid: ""
  ]
```
