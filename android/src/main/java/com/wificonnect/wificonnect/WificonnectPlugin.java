package com.wificonnect.wificonnect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import io.flutter.embedding.android.FlutterActivity;
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
public class WificonnectPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler, PluginRegistry.RequestPermissionsResultListener, ConnectivityManager.OnNetworkActiveListener,WifiStateInterphase {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private ActivityPluginBinding binding;
  private Context appContext;
  private WifiStateChangeBroadCastReceiver receiver=new WifiStateChangeBroadCastReceiver(this);

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "wificonnect");
    channel.setMethodCallHandler(this);


  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

      result.success("Android " + android.os.Build.VERSION.RELEASE);


  }



///////////////////
  /////////////
///////////////////
/////////////
  //check permission
  @RequiresApi(api = Build.VERSION_CODES.M)
  public void checkPermission(){
  if(appContext.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)== PackageManager.PERMISSION_DENIED||appContext.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)==PackageManager.PERMISSION_DENIED)
  {
    requestPermission();
  }
   //initalize();
  }

  ///////////////////
  ////////////////////////////////
  //  /////////////
  // request permission
  ////
  @RequiresApi(api = Build.VERSION_CODES.M)
  public void requestPermission(){
      binding.getActivity().requestPermissions( new String[] {
              android.Manifest.permission.ACCESS_WIFI_STATE,
              Manifest.permission.ACCESS_NETWORK_STATE,
      },0);
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
  ///////////////////
  ////////////////////////////////
  //  ////////////////////////////////
  //  ////////////////////////////////
  //  /////////////
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
    isWifiConnected(appContext);
    registerNetwokChangedListener(appContext);
    //isNetworkAvailable(appContext);
   boolean active= isWifiEnabled(appContext);
     System.out.println(active);

    IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
     appContext.registerReceiver(receiver,intentFilter);
   // checkPermission();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    appContext.unregisterReceiver(receiver);
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

  public void registerNetwokChangedListener(Context context){
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    manager.addDefaultNetworkActiveListener(this);
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


  @Override
  public void onNetworkActive() {
    if(isNetworkAvailable(appContext)){
      System.out.println("network is availabe");
      return;
    }
    System.out.println("network is not avalable");
  }

  @Override
  public void wifiState(boolean state) {
    System.out.println(state);
  }
}
