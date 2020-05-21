package com.ht1.toggle;

import static com.ht1.toggle.MainActivity.STT_ON;

public class DeviceInfo {
    public String Name;
    public String RealName;
    public String Status;
    public String ipAddress;
    public int MissingCount;

    public DeviceInfo(String _Name, String _Status, String _ipAddress) {
        RealName = _Name;
        Name = new String();

        Status = _Status;
        MissingCount = 0;

        ipAddress = _ipAddress;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getRealName() {
        return RealName;
    }

    public boolean getBoolStatus() {
        return (Status.equals(STT_ON));
    }

    public String getStatus() {
        return Status;
    }
}