//  SDLPerformAudioPassThruResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLPerformAudioPassThruResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLPerformAudioPassThruResponse

-(id) init {
    if (self = [super initWithName:NAMES_PerformAudioPassThru]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
