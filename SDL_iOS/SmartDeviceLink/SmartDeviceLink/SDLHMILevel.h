//  SDLHMILevel.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLHMILevel : SDLEnum {}

+(SDLHMILevel*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLHMILevel*) HMI_FULL;
+(SDLHMILevel*) HMI_LIMITED;
+(SDLHMILevel*) HMI_BACKGROUND;
+(SDLHMILevel*) HMI_NONE;

@end
