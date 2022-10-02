package com.wificonnect.wificonnect.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.wificonnect.wificonnect.AppDataHelper;
import com.wificonnect.wificonnect.interphase.Notifier;

public class Location extends BroadcastReceiver {
    private Notifier interphase;
    private AppDataHelper appDataHelper;
    public Location(Notifier instance,AppDataHelper appDataHelper){
        this.interphase=instance;
        this.appDataHelper=appDataHelper;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        //appDataHelper.scanWifi(context);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           interphase.LocationStateChanged(true);
           return;
        }
        interphase.LocationStateChanged(false);
    }
}
