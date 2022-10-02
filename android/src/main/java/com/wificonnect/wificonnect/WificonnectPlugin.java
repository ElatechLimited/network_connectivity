package com.wificonnect.wificonnect;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wificonnect.wificonnect.broadcast.Location;
import com.wificonnect.wificonnect.broadcast.WifiScanList;
import com.wificonnect.wificonnect.broadcast.Wifi;
import com.wificonnect.wificonnect.interphase.Notifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** WificonnectPlugin */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WificonnectPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler, PluginRegistry.RequestPermissionsResultListener, ConnectivityManager.OnNetworkActiveListener, Notifier {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private ActivityPluginBinding binding;
  private Context appContext;
  private AppDataHelper appDataHelper;
  private Wifi WifiStateReceiver;
  private WifiScanList WifiScanListReceiver;
  private Location LocationReciever;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "wificonnect");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

     if(call.method.equals("isWifiEnabled")){
       result.success(appDataHelper.isWifiEnabled(appContext));
       return;
     }
     if(call.method.equals("isLocationEnabled")){
       result.success(appDataHelper.isLocationEnabled(appContext));
       return;
     }
     if(call.method.equals("enableprocess")){
       registerRecievers();
       appDataHelper.enabledProcess=true;
       appDataHelper.scanWifi(appContext);
       result.success(true);
       return;
     }
     if(call.method.equals("disableprocess")){
       appDataHelper.enabledProcess=false;
       appDataHelper.scanWifi(appContext);
       unRegisterReciever();
       result.success(true);
       return;
     }
     if(call.method.equals("enableWifi")){
       appDataHelper.enableWifi(appContext);
       if(appDataHelper.isWifiEnabled(appContext)){
         appDataHelper.scanWifi(appContext);
       }
       result.success(true);
       return;

     }

    if(call.method.equals("scanNetwork")){
      appDataHelper.scanWifi(appContext);
      result.success(true);
      return;
    }
  }



  @Override
  public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if(requestCode==11&&grantResults.length>0){
      for(int x=0; x<grantResults.length; x++){
        if(grantResults[x]!=PackageManager.PERMISSION_GRANTED){
          Toast.makeText(appContext,"Permission Not Yet Granted.",Toast.LENGTH_LONG).show();
        return false;
        }
      }
     // initialize()
  }
    return true;
    }
  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.binding=binding;
    this.appContext=binding.getActivity().getApplicationContext();
    binding.addRequestPermissionsResultListener(this);
    registerNetwokChangedListener(appContext);
    appDataHelper= new AppDataHelper(appContext,binding.getActivity());
     if(!appDataHelper.checkPermission()){
       appDataHelper.requestPermission();
     }

  }

  private void registerRecievers(){

    //set intent for broadcastreciever
    IntentFilter wifiState = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
    IntentFilter wifiList = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    IntentFilter Location = new IntentFilter(LocationManager.MODE_CHANGED_ACTION);
    //init appdatahelper class for intializing recevers


    LocationReciever=new Location(this,appDataHelper);
    WifiStateReceiver=new Wifi(this,appDataHelper);
    WifiScanListReceiver=new WifiScanList(this);

    //register reciever
    appContext.registerReceiver(WifiStateReceiver,wifiState);
    appContext.registerReceiver(WifiScanListReceiver,wifiList);
    appContext.registerReceiver(LocationReciever,Location);


  }

  private void unRegisterReciever(){
    appContext.unregisterReceiver(WifiStateReceiver);
    appContext.unregisterReceiver(WifiScanListReceiver);
    appContext.unregisterReceiver(LocationReciever);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
   unRegisterReciever();
  }




  public void registerNetwokChangedListener(Context context){
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    manager.addDefaultNetworkActiveListener(this);
  }


  @Override
  public void onNetworkActive() {
    if(appDataHelper.isNetworkAvailable(appContext)){
      System.out.println("network is availabe");
      return;
    }
    System.out.println("network is not avalable");
  }


  @Override
  public void wifiStateChanged(boolean state) {
    HashMap<String, Boolean>data=new HashMap<String, Boolean>();
    data.put("state",state);
    channel.invokeMethod("wifistate",data);
  }

  @Override
  public void LocationStateChanged(boolean state) {
    HashMap<String, Boolean>data=new HashMap<String, Boolean>();
    data.put("state",state);
    channel.invokeMethod("locationState",data);
  }
  @Override
  public void WifiListChanged(List<ScanResult> avalaibleNetowrk) {
    String val=new Gson().toJson(avalaibleNetowrk,new  TypeToken<List<ScanResult>>() {}.getType());
    channel.invokeMethod("wifilist", Map.of("data",val));
  }
}
