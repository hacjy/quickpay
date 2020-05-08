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
主工程的WXPayEntryActivity继承fastplay中的WXPayEntryActivity  
``` 
<!-- 微信支付回调函数-->
        <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:screenOrientation="portrait"
            android:launchMode="singleTop">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="${wx_appid}" />
            </intent-filter>
        </activity>
 ```   
# 可能遇到的问题记录  
1、Program type already present: androidx.versionedparcelable.CustomVersionedPa  
主工程没有使用androidX库，library中使用了，导致了这个问题。  
解决办法：在gradle.properties添加一下代码  
``` 
android.useAndroidX=true
android.enableJetifier=true
``` 
2、需要将主工程使用的v7,v4库全部换成AndroidX  
选中工程-右键rename-migrate to AndroidX
