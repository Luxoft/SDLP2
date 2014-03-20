//  SDLShowResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLShowResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLShowResponse

-(id) init {
    if (self = [super initWithName:NAMES_Show]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
