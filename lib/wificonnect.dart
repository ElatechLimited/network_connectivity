
import 'wificonnect_platform_interface.dart';

class Wificonnect {
  Future<String?> getPlatformVersion() {
    return WificonnectPlatform.instance.getPlatformVersion();
  }
}
