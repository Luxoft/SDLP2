//  SDLTireStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLSingleTireStatus.h>
#import <SmartDeviceLink/SDLWarningLightStatus.h>

@interface SDLTireStatus : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLWarningLightStatus* pressureTelltale;
@property(assign) SDLSingleTireStatus* leftFront;
@property(assign) SDLSingleTireStatus* rightFront;
@property(assign) SDLSingleTireStatus* leftRear;
@property(assign) SDLSingleTireStatus* rightRear;
@property(assign) SDLSingleTireStatus* innerLeftRear;
@property(assign) SDLSingleTireStatus* innerRightRear;

@end
