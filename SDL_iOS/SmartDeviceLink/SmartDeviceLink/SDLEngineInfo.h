//  SDLEngineInfo.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLMaintenanceModeStatus.h>

@interface SDLEngineInfo : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* electricFuelConsumption;
@property(assign) NSNumber* stateOfCharge;
@property(assign) SDLMaintenanceModeStatus* fuelMaintenanceMode;
@property(assign) NSNumber* distanceToEmpty;

@end
