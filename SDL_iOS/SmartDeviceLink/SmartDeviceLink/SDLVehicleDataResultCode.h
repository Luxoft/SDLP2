//  SDLVehicleDataResultCode.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLVehicleDataResultCode : SDLEnum {}

+(SDLVehicleDataResultCode*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                 

+(SDLVehicleDataResultCode*) SUCCESS;
+(SDLVehicleDataResultCode*) DISALLOWED;
+(SDLVehicleDataResultCode*) USER_DISALLOWED;
+(SDLVehicleDataResultCode*) INVALID_ID;
+(SDLVehicleDataResultCode*) VEHICLE_DATA_NOT_AVAILABLE;
+(SDLVehicleDataResultCode*) DATA_ALREADY_SUBSCRIBED;
+(SDLVehicleDataResultCode*) DATA_NOT_SUBSCRIBED;
+(SDLVehicleDataResultCode*) IGNORED;

@end


