//  SDLDisplayCapabilities.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLDisplayType.h>

@interface SDLDisplayCapabilities : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLDisplayType* displayType;
@property(assign) NSMutableArray* textFields;
@property(assign) NSMutableArray* mediaClockFormats;
@property(assign) NSNumber* graphicSupported;

@end
