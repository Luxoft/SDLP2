//  SDLAddCommandResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLAddCommandResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLAddCommandResponse

-(id) init {
    if (self = [super initWithName:NAMES_AddCommand]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
