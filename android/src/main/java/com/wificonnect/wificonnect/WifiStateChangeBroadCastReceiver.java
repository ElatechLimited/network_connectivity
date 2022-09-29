package com.wificonnect.wificonnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import java.util.function.Function;

public class WifiStateChangeBroadCastReceiver extends BroadcastReceiver {
    private WifiStateInterphase wifistateinterphase;
    WifiStateChangeBroadCastReceiver(WifiStateInterphase wifistateinterphase){
        this.wifistateinterphase=wifistateinterphase;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

            int wifiState=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
            if(wifiState==WifiManager.WIFI_STATE_ENABLED){
                wifistateinterphase.wifiState(true);


            }
            else{
                wifistateinterphase.wifiState(false);
            }

    }
}
