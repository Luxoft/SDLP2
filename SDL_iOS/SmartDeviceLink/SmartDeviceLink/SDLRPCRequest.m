//  SDLRPCRequest.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLRPCRequest

-(NSNumber*) correlationID {
	return [function objectForKey:NAMES_correlationID];
}

-(void) setCorrelationID:(NSNumber *)corrID {
    if (corrID != nil) {
        [function setObject:corrID forKey:NAMES_correlationID];
    } else {
        [function removeObjectForKey:NAMES_correlationID];
    }
}

@end
