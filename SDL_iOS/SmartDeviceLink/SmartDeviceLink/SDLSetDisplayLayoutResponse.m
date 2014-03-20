//  SDLSetDisplayLayoutResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSetDisplayLayoutResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSetDisplayLayoutResponse

-(id) init {
    if (self = [super initWithName:NAMES_SetDisplayLayout]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
