//  SDLBodyInformation.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLBodyInformation.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLBodyInformation

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setParkBrakeActive:(NSNumber *) parkBrakeActive {
    if (parkBrakeActive != nil) {
        [store setObject:parkBrakeActive forKey:NAMES_parkBrakeActive];
    } else {
        [store removeObjectForKey:NAMES_parkBrakeActive];
    }
}

-(NSNumber*) parkBrakeActive {
    return [store objectForKey:NAMES_parkBrakeActive];
}


-(void) setIgnitionStableStatus:(SDLIgnitionStableStatus*) ignitionStableStatus {
    if (ignitionStableStatus != nil) {
        [store setObject:ignitionStableStatus forKey:NAMES_ignitionStableStatus];
    } else {
        [store removeObjectForKey:NAMES_ignitionStableStatus];
    }
}

-(NSNumber*) ignitionStableStatus {
    return [store objectForKey:NAMES_ignitionStableStatus];
}

-(void) setIgnitionStatus:(SDLIgnitionStatus*) ignitionStatus {
    if (ignitionStatus != nil) {
        [store setObject:ignitionStatus forKey:NAMES_ignitionStatus];
    } else {
        [store removeObjectForKey:NAMES_ignitionStatus];
    }
}

-(NSNumber*) ignitionStatus {
    return [store objectForKey:NAMES_ignitionStatus];
}

-(void) setDriverDoorAjar:(NSNumber *) driverDoorAjar {
    if (driverDoorAjar != nil) {
        [store setObject:driverDoorAjar forKey:NAMES_driverDoorAjar];
    } else {
        [store removeObjectForKey:NAMES_driverDoorAjar];
    }
}

-(NSNumber*) driverDoorAjar {
    return [store objectForKey:NAMES_driverDoorAjar];
}

-(void) setPassengerDoorAjar:(NSNumber *) passengerDoorAjar {
    if (passengerDoorAjar != nil) {
        [store setObject:passengerDoorAjar forKey:NAMES_passengerDoorAjar];
    } else {
        [store removeObjectForKey:NAMES_passengerDoorAjar];
    }
}

-(NSNumber*) passengerDoorAjar {
    return [store objectForKey:NAMES_passengerDoorAjar];
}

-(void) setRearLeftDoorAjar:(NSNumber *) rearLeftDoorAjar {
    if (rearLeftDoorAjar != nil) {
        [store setObject:rearLeftDoorAjar forKey:NAMES_rearLeftDoorAjar];
    } else {
        [store removeObjectForKey:NAMES_rearLeftDoorAjar];
    }
}

-(NSNumber*) rearLeftDoorAjar {
    return [store objectForKey:NAMES_rearLeftDoorAjar];
}

-(void) setRearRightDoorAjar:(NSNumber *) rearRightDoorAjar {
    if (rearRightDoorAjar != nil) {
        [store setObject:rearRightDoorAjar forKey:NAMES_rearRightDoorAjar];
    } else {
        [store removeObjectForKey:NAMES_rearRightDoorAjar];
    }
}

-(NSNumber*) rearRightDoorAjar {
    return [store objectForKey:NAMES_rearRightDoorAjar];
}




@end
