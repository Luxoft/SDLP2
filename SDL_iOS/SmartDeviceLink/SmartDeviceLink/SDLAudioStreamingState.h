//  SDLAudioStreamingState.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLAudioStreamingState : SDLEnum {}

+(SDLAudioStreamingState*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLAudioStreamingState*) AUDIBLE;
+(SDLAudioStreamingState*) ATTENUATED;
+(SDLAudioStreamingState*) NOT_AUDIBLE;

@end
