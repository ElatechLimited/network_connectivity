package com.wificonnect.wificonnect.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

public class WifiScanList extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<ScanResult> results = new ArrayList<>();
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean success = intent.getBooleanExtra(
                WifiManager.EXTRA_RESULTS_UPDATED, false);
        if(success) {
            results = manager.getScanResults();
            System.out.println(results);
        } else {
            results = manager.getScanResults();
            System.out.println(results);

        }
    }
}