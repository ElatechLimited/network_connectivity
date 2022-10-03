package com.wificonnect.wificonnect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.MacAddress;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.wificonnect.wificonnect.interphase.Notifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AppDataHelper {
   private Context context;
   private Activity instance;
   private Notifier notifier;
   private Timer timer1=new java.util.Timer();
    private Timer timer2=new java.util.Timer();
   public boolean enabledProcess=false;
   private int retrying=0;

   AppDataHelper(Context context, Activity instance,Notifier notifier){
       this.context=context;
       this.instance=instance;
       this.notifier=notifier;
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
           timer1.cancel();
           return;
       }
     if(!isWifiEnabled(context)){
         return;
     }
     if(!isLocationEnabled(context)){
         enableLocation(context);
         return;
     }
        timer2.cancel();
        timer1=new java.util.Timer();
         notifier.wifiScanningStart();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        timer1.schedule(
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


     public void connectWifi(Context context,String ssID,String bssID,String networkPass,String capability){
         timer1.cancel();
         timer2=new java.util.Timer();
         notifier.wifiScanningStop();
         timer2.schedule(
                 new java.util.TimerTask() {
                     @Override
                     public void run() {
                       if(isWifiConnected(context)){
                           retrying=0;
                           timer2.cancel();
                            notifier.onWifiConnected();
                            return;
                       }
                       if(retrying==2){
                           timer2.cancel();
                           retrying=0;
                           notifier.onWifiConnectionFailed();
                       }
                       retrying++;

                     }
                 }, 4000,5000
         );
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             final WifiNetworkSuggestion.Builder suggestion =
                     new WifiNetworkSuggestion.Builder();

             suggestion.setSsid(ssID);
             suggestion.setBssid(MacAddress.fromString(bssID));
             suggestion.setIsAppInteractionRequired(true) ;
             suggestion.build();

             if(capability.contains("WPA")){
                 suggestion.setWpa2Passphrase(networkPass);
             }
        final ArrayList<WifiNetworkSuggestion> suggestionsList =
                     new ArrayList<WifiNetworkSuggestion>();
            suggestionsList.add(suggestion.build());
             //WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            //wifiManager.removeNetworkSuggestions(suggestionsList);
            // wifiManager.addNetworkSuggestions(suggestionsList);
             Bundle bundle=new Bundle();
             bundle.putParcelableArrayList(Settings.EXTRA_WIFI_NETWORK_LIST,suggestionsList);
             Intent intent=new Intent(Settings.ACTION_WIFI_ADD_NETWORKS);

             intent.putExtras(bundle);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(intent);

         }

         else{
             android.net.wifi.WifiConfiguration conf = generateConfiguration(ssID, bssID, networkPass, capability, false);
             int updateNetwork = registerWifiNetworkDeprecated(conf);
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

     public   boolean isWifiConnected(Context context) {
       if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
           ConnectivityManager connManager =
                   (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           android.net.NetworkInfo mWifi =
                   connManager != null ? connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) : null;

           return  mWifi != null && mWifi.isConnected();
       }       ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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



    private android.net.wifi.WifiConfiguration generateConfiguration(
            String ssid, String bssid, String password, String security, Boolean isHidden) {
        android.net.wifi.WifiConfiguration conf = new android.net.wifi.WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        conf.hiddenSSID = isHidden != null ? isHidden : false;
        if (bssid != null) {
            conf.BSSID = bssid;
        }

        if (security != null) security = security.toUpperCase();
        else security = "NONE";

        if (security.toUpperCase().equals("WPA")) {

            /// appropriate ciper is need to set according to security type used,
            /// ifcase of not added it will not be able to connect
            conf.preSharedKey = "\"" + password + "\"";

            conf.allowedProtocols.set(android.net.wifi.WifiConfiguration.Protocol.RSN);

            conf.allowedKeyManagement.set(android.net.wifi.WifiConfiguration.KeyMgmt.WPA_PSK);

            conf.status = android.net.wifi.WifiConfiguration.Status.ENABLED;

            conf.allowedGroupCiphers.set(android.net.wifi.WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(android.net.wifi.WifiConfiguration.GroupCipher.CCMP);

            conf.allowedKeyManagement.set(android.net.wifi.WifiConfiguration.KeyMgmt.WPA_PSK);

            conf.allowedPairwiseCiphers.set(android.net.wifi.WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(android.net.wifi.WifiConfiguration.PairwiseCipher.CCMP);

            conf.allowedProtocols.set(android.net.wifi.WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(android.net.wifi.WifiConfiguration.Protocol.WPA);
        } else if (security.equals("WEP")) {
            conf.wepKeys[0] = "\"" + password + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(android.net.wifi.WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(android.net.wifi.WifiConfiguration.GroupCipher.WEP40);
        } else {
            conf.allowedKeyManagement.set(android.net.wifi.WifiConfiguration.KeyMgmt.NONE);
        }

        return conf;
    }


    private int registerWifiNetworkDeprecated(android.net.wifi.WifiConfiguration conf) {
        int updateNetwork = -1;
        int registeredNetwork = -1;
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        /// Remove the existing configuration for this netwrok
        List<android.net.wifi.WifiConfiguration> mWifiConfigList = manager.getConfiguredNetworks();

        if (mWifiConfigList != null) {
            for (android.net.wifi.WifiConfiguration wifiConfig : mWifiConfigList) {
                if (wifiConfig.SSID.equals(conf.SSID)
                        && (wifiConfig.BSSID == null
                        || conf.BSSID == null
                        || wifiConfig.BSSID.equals(conf.BSSID))) {
                    conf.networkId = wifiConfig.networkId;
                    registeredNetwork = wifiConfig.networkId;
                    updateNetwork = manager.updateNetwork(conf);
                }
            }
        }

        /// If network not already in configured networks add new network
        if (updateNetwork == -1) {
            updateNetwork = manager.addNetwork(conf);
            manager.saveConfiguration();
        }

        // Try returning last known valid network id
        if (updateNetwork == -1) {
            return registeredNetwork;
        }

        return updateNetwork;
    }




}
