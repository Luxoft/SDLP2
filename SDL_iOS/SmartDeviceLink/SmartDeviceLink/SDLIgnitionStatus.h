//  SDLIgnitionStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLIgnitionStatus : SDLEnum {}

+(SDLIgnitionStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLIgnitionStatus*) UNKNOWN;
+(SDLIgnitionStatus*) OFF;
+(SDLIgnitionStatus*) ACCESSORY;
+(SDLIgnitionStatus*) RUN;
+(SDLIgnitionStatus*) START;
+(SDLIgnitionStatus*) INVALID;

@end


