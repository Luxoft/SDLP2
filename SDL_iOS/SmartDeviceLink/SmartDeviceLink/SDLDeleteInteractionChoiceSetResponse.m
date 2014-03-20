//  SDLDeleteInteractionChoiceSetResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDeleteInteractionChoiceSetResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLDeleteInteractionChoiceSetResponse

-(id) init {
    if (self = [super initWithName:NAMES_DeleteInteractionChoiceSet]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
