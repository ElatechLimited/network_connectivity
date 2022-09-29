package com.wificonnect.wificonnect;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.wificonnect.wificonnect.broadcast.WifiScanList;
import com.wificonnect.wificonnect.broadcast.WifiStateChange;
import com.wificonnect.wificonnect.interphase.WifiState;

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
public class WificonnectPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler, PluginRegistry.RequestPermissionsResultListener, ConnectivityManager.OnNetworkActiveListener, WifiState {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private ActivityPluginBinding binding;
  private Context appContext;
  private AppDataHelper appDataHelper;
  private WifiStateChange WifiStateReceiver=new WifiStateChange(this);
  private WifiScanList WifiScanListReceiver=new WifiScanList();

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "wificonnect");
    channel.setMethodCallHandler(this);


  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

      result.success("Android " + android.os.Build.VERSION.RELEASE);


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
    IntentFilter wifiState = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
    IntentFilter wifiList = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
     appContext.registerReceiver(WifiStateReceiver,wifiState);
    appContext.registerReceiver(WifiScanListReceiver,wifiList);
     appDataHelper= new AppDataHelper(appContext,binding.getActivity());
     if(!appDataHelper.checkPermission()){
       appDataHelper.requestPermission();
     }

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    appContext.unregisterReceiver(WifiStateReceiver);
    appContext.unregisterReceiver(WifiScanListReceiver);
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
    System.out.println(state);
  }
}
