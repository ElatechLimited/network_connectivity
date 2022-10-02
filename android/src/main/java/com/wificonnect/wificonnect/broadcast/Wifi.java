package com.wificonnect.wificonnect.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.wificonnect.wificonnect.AppDataHelper;
import com.wificonnect.wificonnect.interphase.Notifier;


public class Wifi extends BroadcastReceiver {
    private Notifier interphase;
    private AppDataHelper appDataHelper;
    public Wifi(Notifier instance,AppDataHelper appDataHelper){
        this.interphase=instance;
        this.appDataHelper=appDataHelper;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

            int wifiState=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
            //appDataHelper.scanWifi(context);
            if(wifiState==WifiManager.WIFI_STATE_ENABLED){
                interphase.wifiStateChanged(true);
            }
            else{
                interphase.wifiStateChanged(false);
            }

    }
}
