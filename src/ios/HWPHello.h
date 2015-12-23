#import <Cordova/CDV.h>

@interface HWPHello : CDVPlugin

- (void) greet:(CDVInvokedUrlCommand*)command;
- (void) start:(CDVInvokedUrlCommand*)command;
- (void) getDeviceInfo:(CDVInvokedUrlCommand*)command;
- (void) getPlataforma:(CDVInvokedUrlCommand*)command;

@end
