//  SDLSingleTireStatus.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSingleTireStatus.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSingleTireStatus

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setStatus:(SDLComponentVolumeStatus*) status {
    if (status != nil) {
        [store setObject:status forKey:NAMES_status];
    } else {
        [store removeObjectForKey:NAMES_status];
    }
}

-(NSNumber*) status {
    return [store objectForKey:NAMES_status];
}

@end
