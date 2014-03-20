//  SDLBodyInformation.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLIgnitionStableStatus.h>
#import <SmartDeviceLink/SDLIgnitionStatus.h>

@interface SDLBodyInformation : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* parkBrakeActive;
@property(assign) SDLIgnitionStableStatus* ignitionStableStatus;
@property(assign) SDLIgnitionStatus* ignitionStatus;
@property(assign) NSNumber* driverDoorAjar;
@property(assign) NSNumber* passengerDoorAjar;
@property(assign) NSNumber* rearLeftDoorAjar;
@property(assign) NSNumber* rearRightDoorAjar;

@end
