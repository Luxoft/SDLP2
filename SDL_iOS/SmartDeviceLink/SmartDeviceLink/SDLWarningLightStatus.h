//  SDLWarningLightStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLWarningLightStatus : SDLEnum {}

+(SDLWarningLightStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                 

+(SDLWarningLightStatus*) OFF;
+(SDLWarningLightStatus*) ON;
+(SDLWarningLightStatus*) FLASH;

@end


