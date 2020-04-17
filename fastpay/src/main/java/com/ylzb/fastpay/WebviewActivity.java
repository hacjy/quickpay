package com.ylzb.fastpay;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ylzb.fastpay.pay.IPayListener;
import com.ylzb.fastpay.pay.PayManager;
import com.ylzb.fastpay.util.CLog;
import com.ylzb.fastpay.widgets.MTWebView;

public class WebviewActivity extends Activity implements IPayListener {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URL = "EXTRA_URL";

    private MTWebView mWebView;
    private TextView btnBack;
    private TextView tvTitle;

    private int reservationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
        initListener();
        initData();
    }

    public void initData() {
        showWebView();
    }

    public void initView() {
        mWebView = findViewById(R.id.webView);
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
    }

    public void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        PayManager.getInstance().setPayListener(this);
    }

    @Override
    public void finish() {
        if(mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();//返回上个页面
        }else {
            super.finish();
        }
    }

    private void showWebView() {
        try {
            String title = getIntent().getStringExtra(EXTRA_TITLE);
            tvTitle.setText(title);
            String html = getIntent().getStringExtra(EXTRA_URL);
            CLog.e("weburl",html);

            if (html.startsWith("https") || html. startsWith("http")){
                mWebView.loadUrl(html+"&sdk_type=android");
            }else {
                // 设置WevView要显示的网页
                String htmlPrefix = "<html>\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                        "    <meta name=viewport content=\"width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0\">\n" +
                        "</head>\n" +
                        "<body>";
                String htmlLastfix = "</body>\n" +
                        "</html>";
                if (!html.startsWith("<html>")){
                    html = htmlPrefix + html + htmlLastfix;
                }
                mWebView.loadData(html,"text/html","UTF-8");
            }
            mWebView.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
            //mWebView.getSettings().setBuiltInZoomControls(true); //页面添加缩放按钮
            //mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);   //取消滚动条

            //点击链接由自己处理，而不是新开Android的系统browser响应该链接。
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    try {
                        CLog.e("webview-override-url",url);
                        view.loadUrl(url);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    return true;
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    //系统默认禁止了mixed content和第三方cookie 解决加载https无效的问题
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mWebView.getSettings()
                                .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    }
                    String message = "SSL Certificate error.";
                    switch (error.getPrimaryError()) {
                        case SslError.SSL_UNTRUSTED:
                            message = "The certificate authority is not trusted.";
                            break;
                        case SslError.SSL_EXPIRED:
                            message = "The certificate has expired.";
                            break;
                        case SslError.SSL_IDMISMATCH:
                            message = "The certificate Hostname mismatch.";
                            break;
                        case SslError.SSL_NOTYETVALID:
                            message = "The certificate is not yet valid.";
                            break;
                    }
                    message += "\"SSL Certificate Error\" Do you want to continue anyway?.. YES";
                    CLog.e("WebViewActivity",message);
                    handler.proceed();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    CLog.e("WebViewActivity",error.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean
    onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

    public void payResult(String result){
        String jsFunction = "javascript:zb_nativePayResult(###)";
        jsFunction = jsFunction.replace("###","\'"+ result +"\'");
        CLog.e("onPayResultEvent","call js function："+jsFunction);
        mWebView.loadUrl(jsFunction);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccess() {
        payResult(new Event.PayResultEvent(Event.PayResultEvent.PAY_RESULT_SUCCESS)
                .resultText());
    }

    @Override
    public void onCancel() {
        payResult(new Event.PayResultEvent(Event.PayResultEvent.PAY_RESULT_CANCEL)
                .resultText());
    }

    @Override
    public void onFail() {
        payResult(new Event.PayResultEvent(Event.PayResultEvent.PAY_RESULT_FAILED)
                .resultText());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
