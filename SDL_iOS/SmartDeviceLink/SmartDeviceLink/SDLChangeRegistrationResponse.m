//  SDLChangeRegistrationResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLChangeRegistrationResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLChangeRegistrationResponse

-(id) init {
    if (self = [super initWithName:NAMES_ChangeRegistration]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
