//  SDLGetVehicleDataResponse.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCResponse.h>

#import <SmartDeviceLink/SDLBeltStatus.h>
#import <SmartDeviceLink/SDLBodyInformation.h>
#import <SmartDeviceLink/SDLComponentVolumeStatus.h>
#import <SmartDeviceLink/SDLDeviceStatus.h>
#import <SmartDeviceLink/SDLGPSData.h>
#import <SmartDeviceLink/SDLHeadLampStatus.h>
#import <SmartDeviceLink/SDLPRNDL.h>
#import <SmartDeviceLink/SDLTireStatus.h>
#import <SmartDeviceLink/SDLVehicleDataEventStatus.h>
#import <SmartDeviceLink/SDLWiperStatus.h>

@interface SDLGetVehicleDataResponse : SDLRPCResponse {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLGPSData* gps;
@property(assign) NSNumber* speed;
@property(assign) NSNumber* rpm;
@property(assign) NSNumber* fuelLevel;
@property(assign) SDLComponentVolumeStatus* fuelLevelState;
@property(assign) NSNumber* instantFuelConsumption;
@property(assign) NSNumber* externalTemperature;
@property(assign) NSString* vin;
@property(assign) SDLPRNDL* prndl;
@property(assign) SDLTireStatus* tirePressure;
@property(assign) NSNumber* odometer;
@property(assign) SDLBeltStatus* beltStatus;
@property(assign) SDLBodyInformation* bodyInformation;
@property(assign) SDLDeviceStatus* deviceStatus;
@property(assign) SDLVehicleDataEventStatus* driverBraking;
@property(assign) SDLWiperStatus* wiperStatus;
@property(assign) SDLHeadLampStatus* headLampStatus;
@property(assign) NSNumber* engineTorque;
@property(assign) NSNumber* accPedalPosition;
@property(assign) NSNumber* steeringWheelAngle;

@end
