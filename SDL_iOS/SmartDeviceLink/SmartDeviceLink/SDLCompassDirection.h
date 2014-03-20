//  SDLCompassDirection.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLCompassDirection : SDLEnum {}

+(SDLCompassDirection*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                 

+(SDLCompassDirection*) NORTH;
+(SDLCompassDirection*) NORTHWEST;
+(SDLCompassDirection*) WEST;
+(SDLCompassDirection*) SOUTHWEST;
+(SDLCompassDirection*) SOUTH;
+(SDLCompassDirection*) SOUTHEAST;
+(SDLCompassDirection*) EAST;
+(SDLCompassDirection*) NORTHEAST;

@end
