//  SDLWiperStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLWiperStatus : SDLEnum {}

+(SDLWiperStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLWiperStatus*) OFF;
+(SDLWiperStatus*) AUTO_OFF;
+(SDLWiperStatus*) OFF_MOVING;
+(SDLWiperStatus*) MAN_INT_OFF;
+(SDLWiperStatus*) MAN_INT_ON;
+(SDLWiperStatus*) MAN_LOW;
+(SDLWiperStatus*) MAN_HIGH;
+(SDLWiperStatus*) MAN_FLICK;
+(SDLWiperStatus*) WASH;
+(SDLWiperStatus*) AUTO_LOW;
+(SDLWiperStatus*) AUTO_HIGH;
+(SDLWiperStatus*) COURTESYWIPE;
+(SDLWiperStatus*) AUTO_ADJUST;
+(SDLWiperStatus*) STALLED;
+(SDLWiperStatus*) NO_DATA_EXISTS;

@end

