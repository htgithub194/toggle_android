package com.ht1.toggle;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {

    static java.lang.String TAG = "ahihi";
    final static java.lang.String STT_ON = "1";
    final static java.lang.String STT_OFF = "0";
    final static int UDP_PORT = 8888;

    final static int PACKETIN_SIZE = 256;

    final static String broadcastIp = "255.255.255.255";
    final static String hubGateWayIp = "192.168.94.1";

    WebView webView;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MODEL
        model = new Model();
        model.init(this);

        // View Component
        webView = findViewById(R.id.webview);
        model.getWebAppInterface().setWebView(webView);
        model.setWebView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(model.getWebAppInterface(), "Android");
        webView.loadUrl("http://192.168.1.7:3000/");
    }
}
