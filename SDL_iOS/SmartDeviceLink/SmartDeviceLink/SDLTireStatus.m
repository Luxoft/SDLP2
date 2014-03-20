//  SDLTireStatus.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLTireStatus.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLTireStatus

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setPressureTelltale:(SDLWarningLightStatus *) pressureTelltale {
    if (pressureTelltale != nil) {
        [store setObject:pressureTelltale forKey:NAMES_pressureTelltale];
    } else {
        [store removeObjectForKey:NAMES_pressureTelltale];
    }
}

-(SDLWarningLightStatus*) pressureTelltale {
    return [store objectForKey:NAMES_pressureTelltale];
}

-(void) setLeftFront:(SDLSingleTireStatus*) leftFront {
    if (leftFront != nil) {
        [store setObject:leftFront forKey:NAMES_leftFront];
    } else {
        [store removeObjectForKey:NAMES_leftFront];
    }
}

-(SDLWarningLightStatus*) leftFront {
    return [store objectForKey:NAMES_leftFront];
}

-(void) setRightFront:(SDLSingleTireStatus*) rightFront {
    if (rightFront != nil) {
        [store setObject:rightFront forKey:NAMES_rightFront];
    } else {
        [store removeObjectForKey:NAMES_rightFront];
    }
}

-(SDLWarningLightStatus*) rightFront {
    return [store objectForKey:NAMES_rightFront];
}

-(void) setLeftRear:(SDLSingleTireStatus*) leftRear {
    if (leftRear != nil) {
        [store setObject:leftRear forKey:NAMES_leftRear];
    } else {
        [store removeObjectForKey:NAMES_leftRear];
    }
}

-(SDLWarningLightStatus*) leftRear {
    return [store objectForKey:NAMES_leftRear];
}

-(void) setRightRear:(SDLSingleTireStatus*) rightRear {
    if (rightRear != nil) {
        [store setObject:rightRear forKey:NAMES_rightRear];
    } else {
        [store removeObjectForKey:NAMES_rightRear];
    }
}

-(SDLWarningLightStatus*) rightRear {
    return [store objectForKey:NAMES_rightRear];
}

-(void) setInnerLeftRear:(SDLSingleTireStatus *) innerLeftRear {
    if (innerLeftRear != nil) {
        [store setObject:innerLeftRear forKey:NAMES_innerLeftRear];
    } else {
        [store removeObjectForKey:NAMES_innerLeftRear];
    }
}

-(SDLWarningLightStatus*) innerLeftRear {
    return [store objectForKey:NAMES_innerLeftRear];
}

-(void) setInnerRightRear:(SDLSingleTireStatus*) innerRightRear {
    if (innerRightRear != nil) {
        [store setObject:innerRightRear forKey:NAMES_innerRightRear];
    } else {
        [store removeObjectForKey:NAMES_innerRightRear];
    }
}

-(SDLWarningLightStatus*) innerRightRear {
    return [store objectForKey:NAMES_innerRightRear];
}

@end
