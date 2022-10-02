import 'package:wificonnect/wifi_model.dart';

class WifiController {
  final Function(bool state) wifiStateChanged;
  final Function(bool state) locationStateChanged;
  final Function(List<WifiModel> model) onWifiListChanged;
  WifiController(
      {required this.locationStateChanged,
      required this.onWifiListChanged,
      required this.wifiStateChanged});
}
