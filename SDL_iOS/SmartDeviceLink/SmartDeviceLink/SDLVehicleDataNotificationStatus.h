//  SDLVehicleDataNotificationStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLVehicleDataNotificationStatus : SDLEnum {}

+(SDLVehicleDataNotificationStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLVehicleDataNotificationStatus*) NOT_SUPPORTED;
+(SDLVehicleDataNotificationStatus*) NORMAL;
+(SDLVehicleDataNotificationStatus*) ACTIVE;

@end


