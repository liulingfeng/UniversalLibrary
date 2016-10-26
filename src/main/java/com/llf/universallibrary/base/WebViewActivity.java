package com.llf.universallibrary.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.llf.universallibrary.R;

/**
 * Created by llf on 2016/10/21.
 * WebView加载网页
 * http://www.jianshu.com/p/d2f5ae6b4927
 */

public class WebViewActivity extends BaseActivity {
    public static void lanuch(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private WebView mWebView;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        url = getIntent().getStringExtra("url");
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹出alert
        mWebView.requestFocus();//触摸焦点起作用
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);//屏幕自适应
        webSettings.setSupportZoom(false);  //不支持缩放
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //辅助处理请求，点击链接在本browser中打开
        mWebView.setWebViewClient(new WebViewClient() {
            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//等待证书响应
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(WebViewActivity.this, "没有安装微信，请选择其他支付方式", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                startProgressDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                closeProgressDialog();
            }

        });

        //辅助处理js的对话框，网站图标，网站title，加载进度
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mWebView.loadUrl(url);
    }

    /**
     * 默认按返回键会直接退出app
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        //处理长时间执行js动画导致耗电
        mWebView.getSettings().setJavaScriptEnabled(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
