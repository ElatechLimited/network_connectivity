#import "WificonnectPlugin.h"
#if __has_include(<wificonnect/wificonnect-Swift.h>)
#import <wificonnect/wificonnect-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "wificonnect-Swift.h"
#endif

@implementation WificonnectPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftWificonnectPlugin registerWithRegistrar:registrar];
}
@end
