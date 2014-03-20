//  SDLVehicleDataStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLVehicleDataStatus : SDLEnum {}

+(SDLVehicleDataStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLVehicleDataStatus*) NO_DATA_EXISTS;
+(SDLVehicleDataStatus*) OFF;
+(SDLVehicleDataStatus*) ON;

@end