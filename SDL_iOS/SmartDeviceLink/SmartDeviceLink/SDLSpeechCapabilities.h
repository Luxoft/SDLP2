//  SDLSpeechCapabilities.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLSpeechCapabilities : SDLEnum {}

+(SDLSpeechCapabilities*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLSpeechCapabilities*) TEXT;
+(SDLSpeechCapabilities*) SAPI_PHONEMES;
+(SDLSpeechCapabilities*) LHPLUS_PHONEMES;
+(SDLSpeechCapabilities*) PRE_RECORDED;
+(SDLSpeechCapabilities*) SILENCE;

@end
