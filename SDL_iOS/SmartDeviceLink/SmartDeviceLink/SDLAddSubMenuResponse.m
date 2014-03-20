//  SDLAddSubMenuResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLAddSubMenuResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLAddSubMenuResponse

-(id) init {
    if (self = [super initWithName:NAMES_AddSubMenu]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
