#import "HWPHello.h"
#include <ifaddrs.h>
#include <arpa/inet.h>

@implementation HWPHello

- (void)greet:(CDVInvokedUrlCommand*)command
{

    NSString* callbackId = [command callbackId];
    NSString* name = [[command arguments] objectAtIndex:0];
    NSString* msg = [NSString stringWithFormat: @"Hello, %@", name];

    Boolean bReturn = FALSE;
	struct ifaddrs *interfaces = NULL;
	struct ifaddrs *temp_addr = NULL;
	int success = 0;
	
	// retrieve the current interfaces - returns 0 on success
	success = getifaddrs(&interfaces);
	
	if (success == 0) {
		// Loop through linked list of interfaces
		temp_addr = interfaces;
			while(temp_addr != NULL) {
				if(temp_addr->ifa_addr->sa_family == AF_INET) {
					// Check if interface is en0 which is the wifi connection on the iPhone
					if([[NSString stringWithUTF8String:temp_addr->ifa_name] containsString:@"utun"]) {
						bReturn = true;
					}
				}
				temp_addr = temp_addr->ifa_next;
			}
		}
	
	// Free memory
	freeifaddrs(interfaces);
    
    if (bReturn)
        msg = [NSString stringWithUTF8String:"Verdadero"];
    else
        msg = [NSString stringWithUTF8String:"Falso"];
	        
    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];

    [self success:result callbackId:callbackId];

}

- (void)start:(CDVInvokedUrlCommand*)command
{

    NSString* callbackId = [command callbackId];
    NSString* name = [[command arguments] objectAtIndex:0];
    NSString* msg = [NSString stringWithFormat: @""];

    NSString *mystr=[[NSString alloc] initWithFormat:name];
    NSURL *myurl=[[NSURL alloc] initWithString:mystr];
    [[UIApplication sharedApplication] openURL:myurl];
	   
    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];

    [self success:result callbackId:callbackId];
}

- (void)getDeviceInfo:(CDVInvokedUrlCommand*)command
{

    NSString* callbackId = [command callbackId];
    NSString *msg = @" ";
    
    msg = [UIDevice currentDevice].name;
    msg = [sRetrun stringByAppendingString:@"|"];
    msg = [sRetrun stringByAppendingString:[UIDevice currentDevice].identifierForVendor.UUIDString];

    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];

    [self success:result callbackId:callbackId];
}

- (void)getPlataforma:(CDVInvokedUrlCommand*)command
{

	NSString* msg = [NSString stringWithFormat: @" "];
	NSString* callbackId = [command callbackId];
	
	msg = [NSString stringWithUTF8String:"iOS"];
	
	CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];

    [self success:result callbackId:callbackId];
	
}

@end
