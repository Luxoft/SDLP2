//  SDLMaintenanceModeStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLMaintenanceModeStatus : SDLEnum {}

+(SDLMaintenanceModeStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLMaintenanceModeStatus*) NORMAL;
+(SDLMaintenanceModeStatus*) NEAR;
+(SDLMaintenanceModeStatus*) ACTIVE;
+(SDLMaintenanceModeStatus*) FEATURE_NOT_PRESENT;

@end


