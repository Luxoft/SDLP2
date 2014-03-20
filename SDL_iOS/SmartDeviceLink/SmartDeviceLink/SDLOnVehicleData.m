//  SDLOnVehicleData.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLOnVehicleData.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLOnVehicleData

-(id) init {
    if (self = [super initWithName:NAMES_OnVehicleData]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setGps:(SDLGPSData*) gps {
    if (gps != nil) {
        [parameters setObject:gps forKey:NAMES_gps];
    } else {
        [parameters removeObjectForKey:NAMES_gps];
    }
}

-(SDLGPSData*) gps {
    NSObject* obj = [parameters objectForKey:NAMES_gps];
    if ([obj isKindOfClass:SDLGPSData.class]) {
        return (SDLGPSData*)obj;
    } else {
        return [[[SDLGPSData alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setSpeed:(NSNumber*) speed {
    if (speed != nil) {
        [parameters setObject:speed forKey:NAMES_speed];
    } else {
        [parameters removeObjectForKey:NAMES_speed];
    }
}

-(NSNumber*) speed {
    return [parameters objectForKey:NAMES_speed];
}

-(void) setRpm:(NSNumber*) rpm {
    if (rpm != nil) {
        [parameters setObject:rpm forKey:NAMES_rpm];
    } else {
        [parameters removeObjectForKey:NAMES_rpm];
    }
}

-(NSNumber*) rpm {
    return [parameters objectForKey:NAMES_rpm];
}

-(void) setFuelLevel:(NSNumber*) fuelLevel {
    if (fuelLevel != nil) {
        [parameters setObject:fuelLevel forKey:NAMES_fuelLevel];
    } else {
        [parameters removeObjectForKey:NAMES_fuelLevel];
    }
}

-(NSNumber*) fuelLevel {
    return [parameters objectForKey:NAMES_fuelLevel];
}

-(void) setFuelLevelState:(SDLComponentVolumeStatus*) fuelLevelState {
    if (fuelLevelState != nil) {
        [parameters setObject:fuelLevelState forKey:NAMES_fuelLevelState];
    } else {
        [parameters removeObjectForKey:NAMES_fuelLevelState];
    }
}

-(SDLComponentVolumeStatus*) fuelLevelState {
    NSObject* obj = [parameters objectForKey:NAMES_fuelLevelState];
    if ([obj isKindOfClass:SDLComponentVolumeStatus.class]) {
        return (SDLComponentVolumeStatus*)obj;
    } else {
        return [SDLComponentVolumeStatus valueOf:(NSString*)obj];
    }
}

-(void) setInstantFuelConsumption:(NSNumber*) instantFuelConsumption {
    if (instantFuelConsumption != nil) {
        [parameters setObject:instantFuelConsumption forKey:NAMES_instantFuelConsumption];
    } else {
        [parameters removeObjectForKey:NAMES_instantFuelConsumption];
    }
}

-(NSNumber*) instantFuelConsumption {
    return [parameters objectForKey:NAMES_instantFuelConsumption];
}

-(void) setExternalTemperature:(NSNumber*) externalTemperature {
    if (externalTemperature != nil) {
        [parameters setObject:externalTemperature forKey:NAMES_externalTemperature];
    } else {
        [parameters removeObjectForKey:NAMES_externalTemperature];
    }
}

-(NSNumber*) externalTemperature {
    return [parameters objectForKey:NAMES_externalTemperature];
}

-(void) setVin:(NSString*) vin {
    if (vin != nil) {
        [parameters setObject:vin forKey:NAMES_vin];
    } else {
        [parameters removeObjectForKey:NAMES_vin];
    }
}

-(NSString*) vin {
    return [parameters objectForKey:NAMES_vin];
}

-(void) setPrndl:(SDLPRNDL*) prndl {
    if (prndl != nil) {
        [parameters setObject:prndl forKey:NAMES_prndl];
    } else {
        [parameters removeObjectForKey:NAMES_prndl];
    }
}

-(SDLPRNDL*) prndl {
    NSObject* obj = [parameters objectForKey:NAMES_prndl];
    if ([obj isKindOfClass:SDLPRNDL.class]) {
        return (SDLPRNDL*)obj;
    } else {
        return [SDLPRNDL valueOf:(NSString*)obj];
    }
}

-(void) setTirePressure:(SDLTireStatus*) tirePressure {
    if (tirePressure != nil) {
        [parameters setObject:tirePressure forKey:NAMES_tirePressure];
    } else {
        [parameters removeObjectForKey:NAMES_tirePressure];
    }
}

-(SDLTireStatus*) tirePressure {
    NSObject* obj = [parameters objectForKey:NAMES_tirePressure];
    if ([obj isKindOfClass:SDLTireStatus.class]) {
        return (SDLTireStatus*)obj;
    } else {
        return [[[SDLTireStatus alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setOdometer:(NSNumber*) odometer {
    if (odometer != nil) {
        [parameters setObject:odometer forKey:NAMES_odometer];
    } else {
        [parameters removeObjectForKey:NAMES_odometer];
    }
}

-(NSNumber*) odometer {
    return [parameters objectForKey:NAMES_odometer];
}

-(void) setBeltStatus:(SDLBeltStatus*) beltStatus {
    if (beltStatus != nil) {
        [parameters setObject:beltStatus forKey:NAMES_beltStatus];
    } else {
        [parameters removeObjectForKey:NAMES_beltStatus];
    }
}

-(SDLBeltStatus*) beltStatus {
    NSObject* obj = [parameters objectForKey:NAMES_beltStatus];
    if ([obj isKindOfClass:SDLBeltStatus.class]) {
        return (SDLBeltStatus*)obj;
    } else {
        return [[[SDLBeltStatus alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setBodyInformation:(SDLBodyInformation*) bodyInformation {
    if (bodyInformation != nil) {
        [parameters setObject:bodyInformation forKey:NAMES_bodyInformation];
    } else {
        [parameters removeObjectForKey:NAMES_bodyInformation];
    }
}

-(SDLBodyInformation*) bodyInformation {
    NSObject* obj = [parameters objectForKey:NAMES_bodyInformation];
    if ([obj isKindOfClass:SDLBodyInformation.class]) {
        return (SDLBodyInformation*)obj;
    } else {
        return [[[SDLBodyInformation alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setDeviceStatus:(SDLDeviceStatus*) deviceStatus {
    if (deviceStatus != nil) {
        [parameters setObject:deviceStatus forKey:NAMES_deviceStatus];
    } else {
        [parameters removeObjectForKey:NAMES_deviceStatus];
    }
}

-(SDLDeviceStatus*) deviceStatus {
    NSObject* obj = [parameters objectForKey:NAMES_deviceStatus];
    if ([obj isKindOfClass:SDLDeviceStatus.class]) {
        return (SDLDeviceStatus*)obj;
    } else {
        return [[[SDLDeviceStatus alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setDriverBraking:(SDLVehicleDataEventStatus*) driverBraking {
    if (driverBraking != nil) {
        [parameters setObject:driverBraking forKey:NAMES_driverBraking];
    } else {
        [parameters removeObjectForKey:NAMES_driverBraking];
    }
}

-(SDLVehicleDataEventStatus*) driverBraking {
    NSObject* obj = [parameters objectForKey:NAMES_driverBraking];
    if ([obj isKindOfClass:SDLVehicleDataEventStatus.class]) {
        return (SDLVehicleDataEventStatus*)obj;
    } else {
        return [SDLVehicleDataEventStatus valueOf:(NSString*)obj];
    }
}

-(void) setWiperStatus:(SDLWiperStatus*) wiperStatus {
    if (wiperStatus != nil) {
        [parameters setObject:wiperStatus forKey:NAMES_wiperStatus];
    } else {
        [parameters removeObjectForKey:NAMES_wiperStatus];
    }
}

-(SDLWiperStatus*) wiperStatus {
    NSObject* obj = [parameters objectForKey:NAMES_wiperStatus];
    if ([obj isKindOfClass:SDLWiperStatus.class]) {
        return (SDLWiperStatus*)obj;
    } else {
        return [SDLWiperStatus valueOf:(NSString*)obj];
    }
}

-(void) setHeadLampStatus:(SDLHeadLampStatus*) headLampStatus {
    if (headLampStatus != nil) {
        [parameters setObject:headLampStatus forKey:NAMES_headLampStatus];
    } else {
        [parameters removeObjectForKey:NAMES_headLampStatus];
    }
}

-(SDLHeadLampStatus*) headLampStatus {
    NSObject* obj = [parameters objectForKey:NAMES_headLampStatus];
    if ([obj isKindOfClass:SDLHeadLampStatus.class]) {
        return (SDLHeadLampStatus*)obj;
    } else {
        return [[[SDLHeadLampStatus alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setEngineTorque:(NSNumber*) engineTorque {
    if (engineTorque != nil) {
        [parameters setObject:engineTorque forKey:NAMES_engineTorque];
    } else {
        [parameters removeObjectForKey:NAMES_engineTorque];
    }
}

-(NSNumber*) engineTorque {
    return [parameters objectForKey:NAMES_engineTorque];
}

-(void) setAccPedalPosition:(NSNumber*) accPedalPosition {
    if (accPedalPosition != nil) {
        [parameters setObject:accPedalPosition forKey:NAMES_accPedalPosition];
    } else {
        [parameters removeObjectForKey:NAMES_accPedalPosition];
    }
}

-(NSNumber*) accPedalPosition {
    return [parameters objectForKey:NAMES_accPedalPosition];
}

-(void) setSteeringWheelAngle:(NSNumber*) steeringWheelAngle {
    if (steeringWheelAngle != nil) {
        [parameters setObject:steeringWheelAngle forKey:NAMES_steeringWheelAngle];
    } else {
        [parameters removeObjectForKey:NAMES_steeringWheelAngle];
    }
}

-(NSNumber*) steeringWheelAngle {
    return [parameters objectForKey:NAMES_steeringWheelAngle];
}

@end
