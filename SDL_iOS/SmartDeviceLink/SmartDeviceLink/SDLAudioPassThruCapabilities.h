//  SDLAudioPassThruCapabilities.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLAudioType.h>
#import <SmartDeviceLink/SDLBitsPerSample.h>
#import <SmartDeviceLink/SDLSamplingRate.h>

@interface SDLAudioPassThruCapabilities : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLSamplingRate* samplingRate;
@property(assign) SDLBitsPerSample* bitsPerSample;
@property(assign) SDLAudioType* audioType;

@end
