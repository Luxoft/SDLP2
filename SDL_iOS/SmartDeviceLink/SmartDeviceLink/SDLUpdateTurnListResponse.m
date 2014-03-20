//  SDLUpdateTurnListResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLUpdateTurnListResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLUpdateTurnListResponse

-(id) init {
    if (self = [super initWithName:NAMES_AlertManeuver]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
