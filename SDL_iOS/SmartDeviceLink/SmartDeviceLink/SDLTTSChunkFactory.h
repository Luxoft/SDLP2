//  SDLTTSChunkFactory.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

#import <SmartDeviceLink/SDLJingle.h>
#import <SmartDeviceLink/SDLTTSChunk.h>

@interface SDLTTSChunkFactory : NSObject {}

+(SDLTTSChunk*) buildTTSChunkForString:(NSString*) text type:(SDLSpeechCapabilities*)type;
+(NSMutableArray*) buildTTSChunksFromSimple:(NSString*) simple;

@end
