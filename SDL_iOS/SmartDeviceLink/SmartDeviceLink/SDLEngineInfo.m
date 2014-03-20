//  SDLEngineInfo.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLEngineInfo.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLEngineInfo

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setElectricFuelConsumption:(NSNumber *) electricFuelConsumption {
    if (electricFuelConsumption != nil) {
        [store setObject:electricFuelConsumption forKey:NAMES_electricFuelConsumption];
    } else {
        [store removeObjectForKey:NAMES_electricFuelConsumption];
    }
}

-(NSNumber*) electricFuelConsumption {
    return [store objectForKey:NAMES_electricFuelConsumption];
}

-(void) setStateOfCharge:(NSNumber *) stateOfCharge {
    if (stateOfCharge != nil) {
        [store setObject:stateOfCharge forKey:NAMES_stateOfCharge];
    } else {
        [store removeObjectForKey:NAMES_stateOfCharge];
    }
}

-(NSNumber*) stateOfCharge {
    return [store objectForKey:NAMES_stateOfCharge];
}

-(void) setFuelMaintenanceMode:(SDLMaintenanceModeStatus*) fuelMaintenanceMode {
    if (fuelMaintenanceMode != nil) {
        [store setObject:fuelMaintenanceMode forKey:NAMES_fuelMaintenanceMode];
    } else {
        [store removeObjectForKey:NAMES_fuelMaintenanceMode];
    }
}

-(SDLMaintenanceModeStatus*) fuelMaintenanceMode {
    return [store objectForKey:NAMES_fuelMaintenanceMode];
}

-(void) setDistanceToEmpty:(NSNumber *) distanceToEmpty {
    if (distanceToEmpty != nil) {
        [store setObject:distanceToEmpty forKey:NAMES_distanceToEmpty];
    } else {
        [store removeObjectForKey:NAMES_distanceToEmpty];
    }
}

-(NSNumber*) distanceToEmpty {
    return [store objectForKey:NAMES_distanceToEmpty];
}

@end
