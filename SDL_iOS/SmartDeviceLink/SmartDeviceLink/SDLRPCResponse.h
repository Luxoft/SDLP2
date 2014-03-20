//  SDLRPCResponse.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLResult.h>

@interface SDLRPCResponse : SDLRPCMessage {}

@property(retain) NSNumber* correlationID;

@property(retain) NSNumber* success;
@property(assign) SDLResult* resultCode;
@property(retain) NSString* info;

@end
