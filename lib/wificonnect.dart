import 'package:flutter/services.dart';
import 'package:wificonnect/wifi_controller.dart';
import 'package:location/location.dart';
import 'dart:convert' as convert;

import 'package:wificonnect/wifi_model.dart';

class WifiConnect {
  final MethodChannel _channel = const MethodChannel("wificonnect");
  static WifiConnect? _instance;
  final WifiController controller;
  WifiConnect._({required this.controller}) {
    onMethodCall();
    enableProcess();
  }

  static WifiConnect getInstance({WifiController? controller}) {
    if (_instance == null) {
      _instance = WifiConnect._(controller: controller!);
      return _instance!;
    }
    return _instance!;
  }

  Future<bool> isWifiEnabled() async {
    return await _channel.invokeMethod("isWifiEnabled");
  }

  Future<void> enableProcess() async {
    return await _channel.invokeMethod("enableprocess");
  }

  Future<void> connectToNetwork(String ssID, String bssID,
      String networkPassword, String capability) async {
    await _channel.invokeMethod("connectNetwork", {
      "ssID": ssID,
      "bssID": bssID,
      "networkPassword": networkPassword,
      "capability": capability
    });
    return;
  }

  Future<bool> enabledLocation() async {
    Location location = Location();
    bool serviceEnabled = await location.serviceEnabled();
    if (!serviceEnabled) {
      serviceEnabled = await location.requestService();
    }
    return serviceEnabled;
  }

  Future<void> enableWifi() async {
    await _channel.invokeMethod("enableWifi");
  }

  Future<void> scanNetwork() async {
    await _channel.invokeMethod("scanNetwork");
  }

  Future<bool> isLocationEnabled() async {
    return await _channel.invokeMethod("isLocationEnabled");
  }

  void onMethodCall() {
    _channel.setMethodCallHandler((call) async {
      if (call.method == "wifistate") {
        var state = call.arguments['state'];
        if (state) {
          scanNetwork();
        }
        controller.wifiStateChanged(state);
        return;
      }
      if (call.method == "locationState") {
        var state = call.arguments['state'];
        controller.locationStateChanged(state);
        return;
      }
      if (call.method == "wifilist") {
        List<WifiModel> value = wifiModelFromJson(call.arguments['data']);
        controller.onWifiListChanged(value);
        return;
      }
      if (call.method == "onWifiConntected") {
        controller.onWifiConnected();
      }
      if (call.method == "onConnectionFailed") {
        controller.onWificonnectionFailed();
      }

      return;
    });
  }
}
