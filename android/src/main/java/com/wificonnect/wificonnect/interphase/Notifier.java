package com.wificonnect.wificonnect.interphase;

import android.net.wifi.ScanResult;

import java.util.List;

public interface Notifier {
    public void wifiStateChanged(boolean state);
    public void wifiScanningStart();
    public void wifiScanningStop();
    public void LocationStateChanged(boolean state);
    public void WifiListChanged(List<ScanResult>avalaibleNetowrk);
    public void onWifiConnected();
    public void onWifiConnectionFailed();
}

