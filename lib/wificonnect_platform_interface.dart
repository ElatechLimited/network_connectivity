import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'wificonnect_method_channel.dart';

abstract class WificonnectPlatform extends PlatformInterface {
  /// Constructs a WificonnectPlatform.
  WificonnectPlatform() : super(token: _token);

  static final Object _token = Object();

  static WificonnectPlatform _instance = MethodChannelWificonnect();

  /// The default instance of [WificonnectPlatform] to use.
  ///
  /// Defaults to [MethodChannelWificonnect].
  static WificonnectPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [WificonnectPlatform] when
  /// they register themselves.
  static set instance(WificonnectPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
