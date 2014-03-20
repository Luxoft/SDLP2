//  SDLBeltStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLVehicleDataEventStatus.h>

@interface SDLBeltStatus : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLVehicleDataEventStatus* driverBeltDeployed;
@property(assign) SDLVehicleDataEventStatus* passengerBeltDeployed;
@property(assign) SDLVehicleDataEventStatus* passengerBuckleBelted;
@property(assign) SDLVehicleDataEventStatus* driverBuckleBelted;
@property(assign) SDLVehicleDataEventStatus* leftRow2BuckleBelted;
@property(assign) SDLVehicleDataEventStatus* passengerChildDetected;
@property(assign) SDLVehicleDataEventStatus* rightRow2BuckleBelted;
@property(assign) SDLVehicleDataEventStatus* middleRow2BuckleBelted;
@property(assign) SDLVehicleDataEventStatus* middleRow3BuckleBelted;
@property(assign) SDLVehicleDataEventStatus* leftRow3BuckledBelted;
@property(assign) SDLVehicleDataEventStatus* rightRow3BuckleBelted;
@property(assign) SDLVehicleDataEventStatus* leftRearInflatableBelted;
@property(assign) SDLVehicleDataEventStatus* rightRearInflatableBelted;
@property(assign) SDLVehicleDataEventStatus* middleRow1BeltDeployed;
@property(assign) SDLVehicleDataEventStatus* middleRow1BuckleBelted;

@end
