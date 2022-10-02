package com.wificonnect.wificonnect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

public class AppDataHelper {
   private Context context;
   private Activity instance;
   private Timer timer=new java.util.Timer();
   public boolean enabledProcess=false;


   AppDataHelper(Context context, Activity instance){
       this.context=context;
       this.instance=instance;
   }
    //check permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(){
        if(context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_DENIED||context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED)
        {
          return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission(){
        instance.requestPermissions( new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        },0);
    }


    public void scanWifi(Context context){
       if(!enabledProcess){
           timer.cancel();
           return;
       }
       timer=new java.util.Timer();
     if(!isWifiEnabled(context)){
         return;
     }
     if(!isLocationEnabled(context)){
         enableLocation(context);
         return;
     }
     WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                    wifi.startScan();
                    }
                }, 0,5000
        );
    }

     //check if wifi is turned on or off your device
     public boolean isWifiEnabled(Context context){
         WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
         if (wifi.isWifiEnabled()){

             return true;
         }
         return false;
     }


     public boolean isLocationEnabled(Context context){

         final LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
         if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
             return true;
         }
        return false;
     }

     //Enable Location
     public void enableLocation(Context context){
         final LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
         if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
             instance.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
         }
     }

     public void enableWifi(Context context){
       if(isWifiEnabled(context)){
           return;
       }
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
             instance.startActivityForResult(panelIntent, 0);
         }
     else {
         WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
         wifi.setWifiEnabled(true);
         }
     }

     // check if the wifi is connected to a hotspot or not
     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     public   boolean isWifiConnected(Context context) {
         ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
         info.getState();
         return info != null && info.isConnected() && info.isAvailable();

     }
     //check if the device has internet connection
     public static boolean isNetworkAvailable(Context context) {
         ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo[] info = mgr.getAllNetworkInfo();
         if (info != null) {
             for (int i = 0; i < info.length; i++) {
                 if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                     return true;
                 }
             }
         }
         return false;
     }

}
