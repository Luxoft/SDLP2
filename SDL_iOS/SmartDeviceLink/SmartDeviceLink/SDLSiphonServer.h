//  SDLSiphonServer.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@interface SDLSiphonServer : NSObject <NSStreamDelegate,NSNetServiceDelegate> {}

+ (void)enableSiphonDebug;
+ (void)disableSiphonDebug;
+ (bool)_siphonRawTransportDataFromApp:(const void*) msgBytes msgBytesLength:(int) msgBytesLength;
+ (bool)_siphonRawTransportDataFromSync:(const void*) msgBytes msgBytesLength:(int) msgBytesLength;
+ (bool)_siphonNSLogData:(NSString *) textToLog;
+ (bool)_siphonFormattedTraceData:(NSString*) traceData;
+ (bool)_siphonIsActive;
+ (void)init;
+ (void)dealloc;

@end
