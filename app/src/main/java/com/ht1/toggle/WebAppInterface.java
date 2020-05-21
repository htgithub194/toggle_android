package com.ht1.toggle;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import org.json.JSONException;
import org.json.JSONObject;

public class WebAppInterface {
    Context mContext;
    Model model;

    SendUdpThread sendUdpThread;

    WebView webView;

    final static String TAG = "ahihi";

    WebAppInterface(Context c, Model _model) {
        mContext = c;
        this.model = _model;

        sendUdpThread = model.getSendUdpThread();
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    @JavascriptInterface
    public void eventFromHMI(String stJSON) {
        Log.d(TAG, "eventFromHMI: " + stJSON);
        try {
            JSONObject reader = new JSONObject(stJSON);

            String id = reader.getString("id");
            Log.d(TAG, "id: " + id);

            if(id.equals("get")) {
                sendUdpThread.sendUDP_get();
            }

            if(id.equals("tog")) {
                String dev = reader.getString("dev");
                String stt = reader.getString("stt");
                sendUdpThread.sendUDP("tog " + dev + " " + stt);
            }

            if(id.equals("grp")) {
                String strGroup = reader.getString("groups");
                Log.d(TAG, "strGroup: " + strGroup);
                model.storePreference(Model.PREFERENCE_GROUP, strGroup);
            }

//            if(id.equals("lgr")) {
//                String strGr = model.loadPreference(Model.PREFERENCE_GROUP);
//                Log.d(TAG, "lgr: " + strGr);
//                runJsScript("javascript:document.setGroupList('" + strGr + "')");
//            }
//
//            if(id.equals("ldn")) {
//                String str = model.loadPreference(Model.PREFERENCE_DEVICE_NAME);
//                Log.d(TAG, "ldn: " + str);
//                runJsScript("javascript:document.setDeviceList('" + str + "')");
//            }

            if(id.equals("ini")) {
                String strGr = model.loadPreference(Model.PREFERENCE_GROUP);
                String strDev = model.loadPreference(Model.PREFERENCE_DEVICE_NAME);
                runJsScript("javascript:document.init(" + strGr + "," + strDev + ")");
            }

            if(id.equals("sdn")) {
                String devName = reader.getString("devname");
                Log.d(TAG, "devname: " + devName);
                model.storePreference(Model.PREFERENCE_DEVICE_NAME, devName);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void runJsScript(String script) {
        RunJsOnMainThread runJsOnMainThread = new RunJsOnMainThread();
        runJsOnMainThread.setScript(script);

        new Handler(Looper.getMainLooper()).post(runJsOnMainThread);
    }

    class RunJsOnMainThread implements Runnable {
        String script;

        public void setScript(String script) {
            this.script = new String();
            this.script = script;
        }

        @Override
        public void run() {
            webView.loadUrl(script);
        }
    }
}