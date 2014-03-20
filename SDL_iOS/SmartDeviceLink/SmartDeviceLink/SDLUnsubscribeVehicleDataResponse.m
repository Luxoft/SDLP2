//  SDLUnsubscribeVehicleDataResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLUnsubscribeVehicleDataResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLUnsubscribeVehicleDataResponse

-(id) init {
    if (self = [super initWithName:NAMES_UnsubscribeVehicleData]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setGps:(SDLVehicleDataResult *)gps {
    if (gps != nil) {
        [parameters setObject:gps forKey:NAMES_gps];
    } else {
        [parameters removeObjectForKey:NAMES_gps];
    }
}

-(SDLVehicleDataResult*) gps {
    NSObject* obj = [parameters objectForKey:NAMES_gps];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setSpeed:(SDLVehicleDataResult*) speed {
    if (speed != nil) {
        [parameters setObject:speed forKey:NAMES_speed];
    } else {
        [parameters removeObjectForKey:NAMES_speed];
    }
}

-(SDLVehicleDataResult*) speed {
    NSObject* obj = [parameters objectForKey:NAMES_speed];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setRpm:(SDLVehicleDataResult*) rpm {
    if (rpm != nil) {
        [parameters setObject:rpm forKey:NAMES_rpm];
    } else {
        [parameters removeObjectForKey:NAMES_rpm];
    }
}

-(SDLVehicleDataResult*) rpm {
    NSObject* obj = [parameters objectForKey:NAMES_rpm];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setFuelLevel:(SDLVehicleDataResult*) fuelLevel {
    if (fuelLevel != nil) {
        [parameters setObject:fuelLevel forKey:NAMES_fuelLevel];
    } else {
        [parameters removeObjectForKey:NAMES_fuelLevel];
    }
}

-(SDLVehicleDataResult*) fuelLevel {
    NSObject* obj = [parameters objectForKey:NAMES_fuelLevel];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setFuelLevelState:(SDLVehicleDataResult*) fuelLevelState {
    if (fuelLevelState != nil) {
        [parameters setObject:fuelLevelState forKey:NAMES_fuelLevelState];
    } else {
        [parameters removeObjectForKey:NAMES_fuelLevelState];
    }
}

-(SDLVehicleDataResult*) fuelLevelState {
    NSObject* obj = [parameters objectForKey:NAMES_fuelLevelState];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setInstantFuelConsumption:(SDLVehicleDataResult*) instantFuelConsumption {
    if (instantFuelConsumption != nil) {
        [parameters setObject:instantFuelConsumption forKey:NAMES_instantFuelConsumption];
    } else {
        [parameters removeObjectForKey:NAMES_instantFuelConsumption];
    }
}

-(SDLVehicleDataResult*) instantFuelConsumption {
    NSObject* obj = [parameters objectForKey:NAMES_instantFuelConsumption];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setExternalTemperature:(SDLVehicleDataResult*) externalTemperature {
    if (externalTemperature != nil) {
        [parameters setObject:externalTemperature forKey:NAMES_externalTemperature];
    } else {
        [parameters removeObjectForKey:NAMES_externalTemperature];
    }
}

-(SDLVehicleDataResult*) externalTemperature {
    NSObject* obj = [parameters objectForKey:NAMES_externalTemperature];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setPrndl:(SDLVehicleDataResult*) prndl {
    if (prndl != nil) {
        [parameters setObject:prndl forKey:NAMES_prndl];
    } else {
        [parameters removeObjectForKey:NAMES_prndl];
    }
}

-(SDLVehicleDataResult*) prndl {
    NSObject* obj = [parameters objectForKey:NAMES_prndl];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setTirePressure:(SDLVehicleDataResult*) tirePressure {
    if (tirePressure != nil) {
        [parameters setObject:tirePressure forKey:NAMES_tirePressure];
    } else {
        [parameters removeObjectForKey:NAMES_tirePressure];
    }
}

-(SDLVehicleDataResult*) tirePressure {
    NSObject* obj = [parameters objectForKey:NAMES_tirePressure];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setOdometer:(SDLVehicleDataResult*) odometer {
    if (odometer != nil) {
        [parameters setObject:odometer forKey:NAMES_odometer];
    } else {
        [parameters removeObjectForKey:NAMES_odometer];
    }
}

-(SDLVehicleDataResult*) odometer {
    NSObject* obj = [parameters objectForKey:NAMES_odometer];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setBeltStatus:(SDLVehicleDataResult*) beltStatus {
    if (beltStatus != nil) {
        [parameters setObject:beltStatus forKey:NAMES_beltStatus];
    } else {
        [parameters removeObjectForKey:NAMES_beltStatus];
    }
}

-(SDLVehicleDataResult*) beltStatus {
    NSObject* obj = [parameters objectForKey:NAMES_beltStatus];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setBodyInformation:(SDLVehicleDataResult*) bodyInformation {
    if (bodyInformation != nil) {
        [parameters setObject:bodyInformation forKey:NAMES_bodyInformation];
    } else {
        [parameters removeObjectForKey:NAMES_bodyInformation];
    }
}

-(SDLVehicleDataResult*) bodyInformation {
    NSObject* obj = [parameters objectForKey:NAMES_bodyInformation];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setDeviceStatus:(SDLVehicleDataResult*) deviceStatus {
    if (deviceStatus != nil) {
        [parameters setObject:deviceStatus forKey:NAMES_deviceStatus];
    } else {
        [parameters removeObjectForKey:NAMES_deviceStatus];
    }
}

-(SDLVehicleDataResult*) deviceStatus {
    NSObject* obj = [parameters objectForKey:NAMES_deviceStatus];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setDriverBraking:(SDLVehicleDataResult*) driverBraking {
    if (driverBraking != nil) {
        [parameters setObject:driverBraking forKey:NAMES_driverBraking];
    } else {
        [parameters removeObjectForKey:NAMES_driverBraking];
    }
}

-(SDLVehicleDataResult*) driverBraking {
    NSObject* obj = [parameters objectForKey:NAMES_driverBraking];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setWiperStatus:(SDLVehicleDataResult*) wiperStatus {
    if (wiperStatus != nil) {
        [parameters setObject:wiperStatus forKey:NAMES_wiperStatus];
    } else {
        [parameters removeObjectForKey:NAMES_wiperStatus];
    }
}

-(SDLVehicleDataResult*) wiperStatus {
    NSObject* obj = [parameters objectForKey:NAMES_wiperStatus];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setHeadLampStatus:(SDLVehicleDataResult*) headLampStatus {
    if (headLampStatus != nil) {
        [parameters setObject:headLampStatus forKey:NAMES_headLampStatus];
    } else {
        [parameters removeObjectForKey:NAMES_headLampStatus];
    }
}

-(SDLVehicleDataResult*) headLampStatus {
    NSObject* obj = [parameters objectForKey:NAMES_headLampStatus];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setEngineTorque:(SDLVehicleDataResult*) engineTorque {
    if (engineTorque != nil) {
        [parameters setObject:engineTorque forKey:NAMES_engineTorque];
    } else {
        [parameters removeObjectForKey:NAMES_engineTorque];
    }
}

-(SDLVehicleDataResult*) engineTorque {
    NSObject* obj = [parameters objectForKey:NAMES_engineTorque];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setAccPedalPosition:(SDLVehicleDataResult*) accPedalPosition {
    if (accPedalPosition != nil) {
        [parameters setObject:accPedalPosition forKey:NAMES_accPedalPosition];
    } else {
        [parameters removeObjectForKey:NAMES_accPedalPosition];
    }
}

-(SDLVehicleDataResult*) accPedalPosition {
    NSObject* obj = [parameters objectForKey:NAMES_accPedalPosition];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setSteeringWheelAngle:(SDLVehicleDataResult*) steeringWheelAngle {
    if (steeringWheelAngle != nil) {
        [parameters setObject:steeringWheelAngle forKey:NAMES_steeringWheelAngle];
    } else {
        [parameters removeObjectForKey:NAMES_steeringWheelAngle];
    }
}

-(SDLVehicleDataResult*) steeringWheelAngle {
    NSObject* obj = [parameters objectForKey:NAMES_steeringWheelAngle];
    if ([obj isKindOfClass:SDLVehicleDataResult.class]) {
        return (SDLVehicleDataResult*)obj;
    } else {
        return [[[SDLVehicleDataResult alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

@end
