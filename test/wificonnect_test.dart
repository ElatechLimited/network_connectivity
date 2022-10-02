// import 'package:flutter_test/flutter_test.dart';
// import 'package:wificonnect/wificonnect.dart';
// import 'package:wificonnect/wificonnect_platform_interface.dart';
// import 'package:wificonnect/wificonnect_method_channel.dart';
// import 'package:plugin_platform_interface/plugin_platform_interface.dart';

// class MockWificonnectPlatform
//     with MockPlatformInterfaceMixin
//     implements WificonnectPlatform {

//   @override
//   Future<String?> getPlatformVersion() => Future.value('42');
// }

// void main() {
//   final WificonnectPlatform initialPlatform = WificonnectPlatform.instance;

//   test('$MethodChannelWificonnect is the default instance', () {
//     expect(initialPlatform, isInstanceOf<MethodChannelWificonnect>());
//   });

//   test('getPlatformVersion', () async {
//     Wificonnect wificonnectPlugin = Wificonnect();
//     MockWificonnectPlatform fakePlatform = MockWificonnectPlatform();
//     WificonnectPlatform.instance = fakePlatform;

//     expect(await wificonnectPlugin.getPlatformVersion(), '42');
//   });
// }
