//  SDLButtonCapabilities.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLButtonName.h>

@interface SDLButtonCapabilities : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLButtonName* name;
@property(assign) NSNumber* shortPressAvailable;
@property(assign) NSNumber* longPressAvailable;
@property(assign) NSNumber* upDownAvailable;

@end
