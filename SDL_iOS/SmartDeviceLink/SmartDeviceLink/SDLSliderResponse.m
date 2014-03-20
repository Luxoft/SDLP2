//  SDLSliderResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSliderResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSliderResponse

-(id) init {
    if (self = [super initWithName:NAMES_Slider]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
