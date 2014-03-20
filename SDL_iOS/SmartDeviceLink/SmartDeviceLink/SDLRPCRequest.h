//  SDLRPCRequest.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

@interface SDLRPCRequest : SDLRPCMessage {}

@property(retain) NSNumber* correlationID;

@end
