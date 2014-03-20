//  SDLIgnitionStableStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLIgnitionStableStatus : SDLEnum {}

+(SDLIgnitionStableStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLIgnitionStableStatus*) IGNITION_SWITCH_NOT_STABLE;
+(SDLIgnitionStableStatus*) IGNITION_SWITCH_STABLE;
+(SDLIgnitionStableStatus*) MISSING_FROM_TRANSMITTER;

@end


