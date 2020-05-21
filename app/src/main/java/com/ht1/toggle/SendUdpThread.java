package com.ht1.toggle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.ht1.toggle.MainActivity.TAG;
import static com.ht1.toggle.MainActivity.UDP_PORT;
import static com.ht1.toggle.MainActivity.broadcastIp;

class SendUdpThread extends Thread {
    public Handler mHandler;
    Model model;
    Context context;

    DatagramSocket socketOUT;
    DatagramPacket packetOUT;

    public SendUdpThread(Context context, Model _mode) {
        this.context = context;
        model = _mode;

        try {
            socketOUT = new DatagramSocket();
            socketOUT.setBroadcast(true);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Co loi xay ra (send)", Toast.LENGTH_LONG).show();
        }
    }

    public void run() {
        Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message message) {
                String msg = message.getData().getString("udp");

                Log.d(TAG, "handleMessage: " + msg);
                try {
                    packetOUT = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(broadcastIp), UDP_PORT);
                    socketOUT.send(packetOUT);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        Looper.loop();
    }

    public void sendUDP_get() {
        String msg = "get " + model.getReceiveUdpThread().getSocketInPort();
        sendUDP(msg);
    }

    public void sendUDP(String msg) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("udp", msg);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
}