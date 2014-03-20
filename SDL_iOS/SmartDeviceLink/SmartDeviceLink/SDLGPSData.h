//  SDLGPSData.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLCompassDirection.h>
#import <SmartDeviceLink/SDLDimension.h>

@interface SDLGPSData : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* longitudeDegrees;
@property(assign) NSNumber* latitudeDegrees;
@property(assign) NSNumber* utcYear;
@property(assign) NSNumber* utcMonth;
@property(assign) NSNumber* utcDay;
@property(assign) NSNumber* utcHours;
@property(assign) NSNumber* utcMinutes;
@property(assign) NSNumber* utcSeconds;
@property(assign) SDLCompassDirection* compassDirection;
@property(assign) NSNumber* pdop;
@property(assign) NSNumber* hdop;
@property(assign) NSNumber* vdop;
@property(assign) NSNumber* actual;
@property(assign) NSNumber* satellites;
@property(assign) SDLDimension* dimension;
@property(assign) NSNumber* altitude;
@property(assign) NSNumber* heading;
@property(assign) NSNumber* speed;

@end
