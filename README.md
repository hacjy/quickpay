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
（三）在Androidmainfest的application设置下属性
```  
    <application
        tools:replace="android:appComponentFactory"
        android:appComponentFactory="@string/app_name">
```  
# 可能遇到的问题记录  
1、Program type already present: androidx.versionedparcelable.CustomVersionedPa  
主工程没有使用androidX库，library中使用了，导致了这个问题。  
解决办法：在gradle.properties添加一下代码  
``` 
android.useAndroidX=true
android.enableJetifier=true
``` 
