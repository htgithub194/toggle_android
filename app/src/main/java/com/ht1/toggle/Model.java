package com.ht1.toggle;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebView;

import java.util.List;
import java.util.Vector;

public class Model {
    final static String PREFERENCE_GROUP = "GROUP";
    final static String PREFERENCE_DEVICE_NAME = "DEV_NAME";
    List<DeviceInfo> deviceInfos;
    List<WifiInfo> wifiInfos;
    List<Group> groupInfos;

    SendUdpThread sendUdpThread;
    ReceiveUdpThread receiveUdpThread;
    WebAppInterface webAppInterface;

    WebView webView;

    Context context;

    public void init(Context context) {
        this.context = context;
        this.deviceInfos = new Vector<>();
        this.wifiInfos = new Vector<>();
        this.groupInfos = new Vector<>();

        receiveUdpThread = new ReceiveUdpThread(context, this);
        sendUdpThread = new SendUdpThread(context, this);

        receiveUdpThread.start();
        sendUdpThread.start();

        webAppInterface = new WebAppInterface(context, this);
    }

    public List<WifiInfo> getWifiInfos() {
        return wifiInfos;
    }

    public ReceiveUdpThread getReceiveUdpThread() {
        return receiveUdpThread;
    }

    public SendUdpThread getSendUdpThread() {
        return sendUdpThread;
    }

    public WebAppInterface getWebAppInterface() {
        return webAppInterface;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public WebView getWebView() {
        return webView;
    }

    public void storePreference(String key, String strGroup) {
        SharedPreferences sharedPref = context.getSharedPreferences("ht_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, strGroup);
        editor.commit();
    }

    public String loadPreference(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("ht_preference", Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }
}
