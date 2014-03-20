//  SDLSyncPData.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSyncPData.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSyncPData

-(id) init {
    if (self = [super initWithName:NAMES_SyncPData]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}


@end
