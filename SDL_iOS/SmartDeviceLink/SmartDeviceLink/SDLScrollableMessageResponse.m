//  SDLScrollableMessageResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLScrollableMessageResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLScrollableMessageResponse

-(id) init {
    if (self = [super initWithName:NAMES_ScrollableMessage]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
