package com.wificonnect.wificonnect.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.wificonnect.wificonnect.interphase.Notifier;

import java.util.ArrayList;
import java.util.List;

public class WifiScanList extends BroadcastReceiver {
    Notifier interphase;
   public WifiScanList(Notifier interphase){
        this.interphase=interphase;
    }
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
            interphase.WifiListChanged(results);


        }
    }
}