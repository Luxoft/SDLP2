//  SDLGetVehicleData.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLVehicleDataType.h>

@interface SDLGetVehicleData : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* gps;
@property(assign) NSNumber* speed;
@property(assign) NSNumber* rpm;
@property(assign) NSNumber* fuelLevel;
@property(assign) NSNumber* fuelLevelState;
@property(assign) NSNumber* instantFuelConsumption;
@property(assign) NSNumber* externalTemperature;
@property(assign) NSNumber* vin;
@property(assign) NSNumber* prndl;
@property(assign) NSNumber* tirePressure;
@property(assign) NSNumber* odometer;
@property(assign) NSNumber* beltStatus;
@property(assign) NSNumber* bodyInformation;
@property(assign) NSNumber* deviceStatus;
@property(assign) NSNumber* driverBraking;
@property(assign) NSNumber* wiperStatus;
@property(assign) NSNumber* headLampStatus;
@property(assign) NSNumber* engineTorque;
@property(assign) NSNumber* accPedalPosition;
@property(assign) NSNumber* steeringWheelAngle;

@end
