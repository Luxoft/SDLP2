//  SDLOnEncodedSyncPData.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCNotification.h>

@interface SDLOnEncodedSyncPData: SDLRPCNotification {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSMutableArray* data;
@property(assign) NSString* url;
@property(assign) NSNumber* timeout;

@end