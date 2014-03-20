//  SDLPerformAudioPassThru.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLAudioType.h>
#import <SmartDeviceLink/SDLBitsPerSample.h>
#import <SmartDeviceLink/SDLSamplingRate.h>

@interface SDLPerformAudioPassThru : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSMutableArray* initialPrompt;
@property(assign) NSString* audioPassThruDisplayText1;
@property(assign) NSString* audioPassThruDisplayText2;
@property(assign) SDLSamplingRate* samplingRate;
@property(assign) NSNumber* maxDuration;
@property(assign) SDLBitsPerSample* bitsPerSample;
@property(assign) SDLAudioType* audioType;
@property(assign) NSNumber* muteAudio;

@end