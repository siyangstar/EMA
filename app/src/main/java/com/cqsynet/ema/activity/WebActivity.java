/*
 * Copyright (C) 2014 重庆尚渝
 * 版权所有
 *
 * 功能描述：显示网页,打开网页先获取cmsId后才能做其它操作
 *
 *
 * 创建标识：zhaosy 20150420
 */
package com.cqsynet.ema.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cqsynet.ema.R;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.util.SharedPreferencesUtil;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity implements OnClickListener {

    private WebView mWebView;
    private String mUrl;
    private String mCategory;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        TextView tvTitle = findViewById(R.id.tvTitle_titlebar);
        ImageButton ibtnBack = findViewById(R.id.ibtnLeft_titlebar);
        ibtnBack.setImageResource(R.drawable.btn_back_selector);
        ibtnBack.setVisibility(View.VISIBLE);
        ibtnBack.setOnClickListener(this);

        mUrl = getIntent().getStringExtra("url");
        mCategory = getIntent().getStringExtra("category");
        mId = getIntent().getStringExtra("id");
        tvTitle.setText(getIntent().getStringExtra("title"));

        mWebView = findViewById(R.id.wv_activity_web);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setTextZoom(100);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setGeolocationDatabasePath(getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        //测试用
//        if(Build.VERSION.SDK_INT >= 19) {
//            mWebView.setWebContentsDebuggingEnabled(true);
//        }

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        mWebView.addJavascriptInterface(new Object() {
            /**
             * 向页面传递服务器地址
             * @return
             */
            @JavascriptInterface
            public String getBaseURL() {
                return AppConstants.URL_MAIN;
            }

            /**
             * 向页面传递sessionId
             * @return
             */
            @JavascriptInterface
            public String getSessionID() {
                return SharedPreferencesUtil.getTagString(WebActivity.this, SharedPreferencesUtil.SEESION_ID);
            }

            /**
             * 传递工单号
             * @return
             */
            @JavascriptInterface
            public String getWorkOrderID() {
                return mId;
            }

            /**
             * 传递资产编号
             * @return
             */
            @JavascriptInterface
            public String getAssetID() {
                return mId;
            }
        }, "eam");

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // 解决5.0以上手机WebView无法成功同步Cookie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }

        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void onDestroy() {
        mWebView.loadData("", "text/html", "utf-8");
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnLeft_titlebar:
                onBackPressed();
                break;
        }
    }
}
