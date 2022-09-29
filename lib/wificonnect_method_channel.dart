import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'wificonnect_platform_interface.dart';

/// An implementation of [WificonnectPlatform] that uses method channels.
class MethodChannelWificonnect extends WificonnectPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('wificonnect');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
