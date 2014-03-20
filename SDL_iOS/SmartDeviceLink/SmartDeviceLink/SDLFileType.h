//  SDLFileType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLFileType : SDLEnum {}

+(SDLFileType*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLFileType*) GRAPHIC_BMP;
+(SDLFileType*) GRAPHIC_JPEG;
+(SDLFileType*) GRAPHIC_PNG;
+(SDLFileType*) AUDIO_WAVE;
+(SDLFileType*) AUDIO_MP3;

@end