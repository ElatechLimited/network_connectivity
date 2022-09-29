import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:wificonnect/wificonnect_method_channel.dart';

void main() {
  MethodChannelWificonnect platform = MethodChannelWificonnect();
  const MethodChannel channel = MethodChannel('wificonnect');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
