//  SDLDriverDistractionState.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLDriverDistractionState : SDLEnum {}

+(SDLDriverDistractionState*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLDriverDistractionState*) DD_ON; 
+(SDLDriverDistractionState*) DD_OFF;

@end
