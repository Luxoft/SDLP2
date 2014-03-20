//  SDLStartTime.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLStartTime.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLStartTime

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setHours:(NSNumber*) hours {
    if (hours != nil) {
        [store setObject:hours forKey:NAMES_hours];
    } else {
        [store removeObjectForKey:NAMES_hours];
    }
}

-(NSNumber*) hours {
    return [store objectForKey:NAMES_hours];
}

-(void) setMinutes:(NSNumber*) minutes {
    if (minutes != nil) {
        [store setObject:minutes forKey:NAMES_minutes];
    } else {
        [store removeObjectForKey:NAMES_minutes];
    }
}

-(NSNumber*) minutes {
    return [store objectForKey:NAMES_minutes];
}

-(void) setSeconds:(NSNumber*) seconds {
    if (seconds != nil) {
        [store setObject:seconds forKey:NAMES_seconds];
    } else {
        [store removeObjectForKey:NAMES_seconds];
    }
}

-(NSNumber*) seconds {
    return [store objectForKey:NAMES_seconds];
}

@end
