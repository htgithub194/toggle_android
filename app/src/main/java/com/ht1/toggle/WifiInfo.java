package com.ht1.toggle;

public class WifiInfo {
    String SSID;
    int signal;
    boolean isChosen;

    WifiInfo(String _ssid, int _signal) {
        SSID = _ssid;
        signal = _signal;
        isChosen = false;
    }

    public int getSignal() {
        return signal;
    }

    public String getSSID() {
        return SSID;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
