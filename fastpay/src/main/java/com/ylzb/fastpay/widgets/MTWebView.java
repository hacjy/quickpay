package com.ylzb.fastpay.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ylzb.fastpay.JsCallAction;
import com.ylzb.fastpay.R;

/**
 * 自定义Webview
 */
public class MTWebView extends WebView {
    private Context context;
    private ProgressBar progressbar;

    public MTWebView(Context context) {
        super(context);
        initProgressBar(context);
        set();
    }

    @SuppressWarnings("deprecation")
    public MTWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressBar(context);
        set();
    }

    private void initProgressBar(Context context) {
        this.context = context;
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_bg));
        progressbar.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progressbar_bg));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 6, 0, 0));
        addView(progressbar);
    }

    @SuppressLint("JavascriptInterface")
    private void set() {
        clearCache(true);
        clearHistory();

        setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            getSettings().setDisplayZoomControls(false);
        }
        getSettings().setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //允许混合（http，https）
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setGeolocationEnabled(true);
        getSettings().setAppCacheEnabled(true);
        //因为h5页没有设置cache-control,所以不缓存才可以实时的获取最新的html内容
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        addJavascriptInterface(new JsCallAction(),"android");

        setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if (context != null && context instanceof Activity && !((Activity) context).isFinishing()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok,
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // do your stuff
                                            result.confirm();
                                        }
                                    }).setCancelable(false).create().show();
                    return true;
                }else {
                    return false;
                }
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.cancel();
                                    }
                                })
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      final JsPromptResult result) {
                return false;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressbar.setVisibility(GONE);
                } else {
                    if (progressbar.getVisibility() == GONE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(progressbar!=null){
            LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
            lp.x = l;
            lp.y = t;
            progressbar.setLayoutParams(lp);
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }
}
