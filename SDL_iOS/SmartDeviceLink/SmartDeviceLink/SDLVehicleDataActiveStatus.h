//  SDLVehicleDataActiveStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLVehicleDataActiveStatus : SDLEnum {}

+(SDLVehicleDataActiveStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLVehicleDataActiveStatus*) INACTIVE_NOT_CONFIRMED;
+(SDLVehicleDataActiveStatus*) INACTIVE_CONFIRMED;
+(SDLVehicleDataActiveStatus*) ACTIVE_NOT_CONFIRMED;
+(SDLVehicleDataActiveStatus*) ACTIVE_CONFIRMED;
+(SDLVehicleDataActiveStatus*) FAULT;

@end


