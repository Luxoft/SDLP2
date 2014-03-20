//  SDLDeleteSubMenuResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDeleteSubMenuResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLDeleteSubMenuResponse

-(id) init {
    if (self = [super initWithName:NAMES_DeleteSubMenu]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end