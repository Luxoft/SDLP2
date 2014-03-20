//  SDLPrimaryAudioSource.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLPrimaryAudioSource : SDLEnum {}

+(SDLPrimaryAudioSource*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLPrimaryAudioSource*) NO_SOURCE_SELECTED;
+(SDLPrimaryAudioSource*) USB;
+(SDLPrimaryAudioSource*) USB2;
+(SDLPrimaryAudioSource*) BLUETOOTH_STEREO_BTST;
+(SDLPrimaryAudioSource*) LINE_IN;
+(SDLPrimaryAudioSource*) IPOD;
+(SDLPrimaryAudioSource*) MOBILE_APP;

@end


