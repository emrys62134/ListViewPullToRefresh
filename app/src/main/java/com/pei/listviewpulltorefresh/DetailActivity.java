package com.pei.listviewpulltorefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dllo on 16/12/1.
 */
public class DetailActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        webView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        webView.loadUrl(data);

    }
}
