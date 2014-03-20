//  SDLResetGlobalPropertiesResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLResetGlobalPropertiesResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLResetGlobalPropertiesResponse

-(id) init {
    if (self = [super initWithName:NAMES_ResetGlobalProperties]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
