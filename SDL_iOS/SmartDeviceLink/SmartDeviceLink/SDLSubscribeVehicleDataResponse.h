//  SDLSubscribeVehicleDataResponse.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCResponse.h>

#import <SmartDeviceLink/SDLVehicleDataResult.h>

@interface SDLSubscribeVehicleDataResponse : SDLRPCResponse {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLVehicleDataResult* gps;
@property(assign) SDLVehicleDataResult* speed;
@property(assign) SDLVehicleDataResult* rpm;
@property(assign) SDLVehicleDataResult* fuelLevel;
@property(assign) SDLVehicleDataResult* fuelLevelState;
@property(assign) SDLVehicleDataResult* instantFuelConsumption;
@property(assign) SDLVehicleDataResult* externalTemperature;
@property(assign) SDLVehicleDataResult* prndl;
@property(assign) SDLVehicleDataResult* tirePressure;
@property(assign) SDLVehicleDataResult* odometer;
@property(assign) SDLVehicleDataResult* beltStatus;
@property(assign) SDLVehicleDataResult* bodyInformation;
@property(assign) SDLVehicleDataResult* deviceStatus;
@property(assign) SDLVehicleDataResult* driverBraking;
@property(assign) SDLVehicleDataResult* wiperStatus;
@property(assign) SDLVehicleDataResult* headLampStatus;
@property(assign) SDLVehicleDataResult* engineTorque;
@property(assign) SDLVehicleDataResult* accPedalPosition;
@property(assign) SDLVehicleDataResult* steeringWheelAngle;

@end
