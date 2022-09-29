package com.wificonnect.wificonnect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

 public class AppDataHelper {
   private Context context;
   private Activity instance;


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


     //check if wifi is turned on or off your device
     public boolean isWifiEnabled(Context context){
         WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

         if (wifi.isWifiEnabled()){
             return true;
         }
         return false;
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
