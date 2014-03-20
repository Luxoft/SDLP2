//  SDLUnsubscribeButtonResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLUnsubscribeButtonResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLUnsubscribeButtonResponse

-(id) init {
    if (self = [super initWithName:NAMES_UnsubscribeButton]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
