//  SDLSpeakResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSpeakResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSpeakResponse

-(id) init {
    if (self = [super initWithName:NAMES_Speak]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
