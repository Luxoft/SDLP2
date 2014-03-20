//  SDLAudioType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLAudioType : SDLEnum {}

+(SDLAudioType*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLAudioType*) PCM;

@end
