package com.ht1.toggle;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

import static com.ht1.toggle.MainActivity.PACKETIN_SIZE;
import static com.ht1.toggle.MainActivity.STT_ON;
import static com.ht1.toggle.MainActivity.TAG;

class ReceiveUdpThread extends Thread {

    Model model;
    DatagramSocket socketIN;
    Context context;
    DatagramPacket packetIN;
    byte[] message;

    public ReceiveUdpThread(Context context, Model _model) {
        this.context = context;
        this.model = _model;

        message = new byte[PACKETIN_SIZE];
        packetIN = new DatagramPacket(message, message.length);

        try {
            socketIN = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Co loi xay ra, xin khoi dong lai!", Toast.LENGTH_LONG).show();
        }
    }

    public int getSocketInPort() {
        return socketIN.getLocalPort();
    }

    public void run() {
        while (true) {
            try {
                socketIN.receive(packetIN);
                String text = new String(packetIN.getData(), 0, packetIN.getLength());
                Log.d(TAG, "receive: " + text);

                String[] separated = text.split("#");
                if (separated.length > 0 && separated[0].equals("ACK")) {

                    // WIFI
                    if (separated.length > 1 && separated[1].equals("wif")) {
                        String strWifiList = text.substring(new String("ACK#wif#").length());
                        Log.d(TAG, "strWifiList: " + strWifiList);

                        String[] wifiList = strWifiList.split("&HT_SPACE&");

                        Log.d(TAG, "wifiList size: " + wifiList.length);

                        List<WifiInfo> wifiInfos = model.getWifiInfos();

                        wifiInfos.clear();

                        for(int k = 0; k < wifiList.length; k+=2) {
                            Log.d(TAG, "k: " + k + " " + wifiList[k]);
                            wifiInfos.add(new WifiInfo(wifiList[k], Integer.parseInt(wifiList[k+1])));
                        }

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                        wifiAdapter.notifyDataSetChanged();
//                            }
//                        });

                        continue;
                    }

                    // GET
                    JSONArray array = new JSONArray();
                    for (int i = 1; i < separated.length; i++) {
                        String[] cmd = separated[i].split(" ");

                        Log.d(TAG, "run: " + packetIN.getAddress().toString().substring(1));
                        Log.d(TAG, "newDev: " + cmd[0]);
                        Log.d(TAG, "newDev: " + cmd[1]);
                        Log.d(TAG, "newDev: " + packetIN.getAddress().toString().substring(1));

                        JSONObject json = new JSONObject();
                        json.put("realName", cmd[0]);
                        json.put("isSttOn", cmd[1].equals(STT_ON));
                        json.put("ip", packetIN.getAddress().toString().substring(1));

                        array.put(json);
                    }
                    // notify to HMI device list changed
                    runJsScript("javascript:document.setDeviceList(" + array.toString() + ")");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runJsScript(String script) {
        Task_UpdateDevices task = new Task_UpdateDevices();
        task.setScript(script);

        new Handler(Looper.getMainLooper()).post(task);
    }

    class Task_UpdateDevices implements Runnable {
        String script;

        public void setScript(String script) {
            this.script = new String();
            this.script = script;
        }

        @Override
        public void run() {
            model.getWebView().loadUrl(script);
        }
    }
}