package com.wificonnect.wificonnect.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.wificonnect.wificonnect.interphase.WifiState;


public class WifiStateChange extends BroadcastReceiver {
    private WifiState interphase;
    public WifiStateChange(WifiState instance){
        this.interphase=instance;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

            int wifiState=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
            if(wifiState==WifiManager.WIFI_STATE_ENABLED){
                interphase.wifiStateChanged(true);
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifi.startScan();
            }
            else{
                interphase.wifiStateChanged(false);
            }

    }
}
