//  SDLComponentVolumeStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLComponentVolumeStatus : SDLEnum {}

+(SDLComponentVolumeStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                 

+(SDLComponentVolumeStatus*) UNKNOWN;
+(SDLComponentVolumeStatus*) NORMAL;
+(SDLComponentVolumeStatus*) LOW;
+(SDLComponentVolumeStatus*) FAULT;
+(SDLComponentVolumeStatus*) ALERT;
+(SDLComponentVolumeStatus*) NOT_SUPPORTED;

@end
