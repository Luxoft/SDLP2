//  SDLOnHMIStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCNotification.h>

#import <SmartDeviceLink/SDLAudioStreamingState.h>
#import <SmartDeviceLink/SDLHMILevel.h>
#import <SmartDeviceLink/SDLSystemContext.h>

@interface SDLOnHMIStatus : SDLRPCNotification {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLHMILevel* hmiLevel;
@property(assign) SDLAudioStreamingState* audioStreamingState;
@property(assign) SDLSystemContext* systemContext;

@end
