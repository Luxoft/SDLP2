//  SDLEndAudioPassThruResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLEndAudioPassThruResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLEndAudioPassThruResponse

-(id) init {
    if (self = [super initWithName:NAMES_EndAudioPassThru]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
