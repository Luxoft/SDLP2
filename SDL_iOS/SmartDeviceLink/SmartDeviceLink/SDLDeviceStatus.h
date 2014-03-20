//  SDLDeviceStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLDeviceLevelStatus.h>
#import <SmartDeviceLink/SDLPrimaryAudioSource.h>

@interface SDLDeviceStatus : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* voiceRecOn;
@property(assign) NSNumber* btIconOn;
@property(assign) NSNumber* callActive;
@property(assign) NSNumber* phoneRoaming;
@property(assign) NSNumber* textMsgAvailable;
@property(assign) SDLDeviceLevelStatus* battLevelStatus;
@property(assign) NSNumber* stereoAudioOutputMuted;
@property(assign) NSNumber* monoAudioOutputMuted;
@property(assign) SDLDeviceLevelStatus* signalLevelStatus;
@property(assign) SDLPrimaryAudioSource* primaryAudioSource;
@property(assign) NSNumber* eCallEventActive;

@end