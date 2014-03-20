//  SDLEncodedSyncPDataResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLEncodedSyncPDataResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLEncodedSyncPDataResponse

-(id) init {
    if (self = [super initWithName:NAMES_EncodedSyncPData]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

@end
