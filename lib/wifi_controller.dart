import 'package:wificonnect/wifi_model.dart';

class WifiController {
  final Function(bool state) wifiStateChanged;
  final Function(bool state) locationStateChanged;
  final Function(List<WifiModel> model) onWifiListChanged;
  final Function onWifiConnected;
  final Function onWificonnectionFailed;
  final Function onWifiScanningStart;
  final Function onWifiScanningStopped;
  WifiController(
      {required this.locationStateChanged,
      required this.onWifiListChanged,
      required this.wifiStateChanged,
      required this.onWifiScanningStart,
      required this.onWifiScanningStopped,
      required this.onWifiConnected,
      required this.onWificonnectionFailed});
}
