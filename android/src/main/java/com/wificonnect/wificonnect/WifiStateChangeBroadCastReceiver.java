package com.wificonnect.wificonnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class WifiStateChangeBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            int wifiState=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
            if(wifiState==WifiManager.WIFI_STATE_ENABLED){
                System.out.println("wifi enabled");
            }
            else{
                System.out.println("Wifi Disabled");
            }

    }
}
