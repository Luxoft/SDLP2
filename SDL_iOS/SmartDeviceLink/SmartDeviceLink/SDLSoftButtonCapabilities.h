//  SDLSoftButtonCapabilities.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

@interface SDLSoftButtonCapabilities : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* shortPressAvailable;
@property(assign) NSNumber* longPressAvailable;
@property(assign) NSNumber* upDownAvailable;
@property(assign) NSNumber* imageSupported;

@end
