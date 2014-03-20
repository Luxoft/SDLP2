//  SDLUnregisterAppInterface.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLUnregisterAppInterface.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLUnregisterAppInterface

-(id) init {
    if (self = [super initWithName:NAMES_UnregisterAppInterface]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
