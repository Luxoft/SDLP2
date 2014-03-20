//  SDLSetMediaClockTimer.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLStartTime.h>
#import <SmartDeviceLink/SDLUpdateMode.h>

@interface SDLSetMediaClockTimer : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLStartTime* startTime;
@property(assign) SDLUpdateMode* updateMode;

@end
