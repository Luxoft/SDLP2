//  SDLSystemContext.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLSystemContext : SDLEnum {}

+(SDLSystemContext*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLSystemContext*) MAIN;
+(SDLSystemContext*) VRSESSION;
+(SDLSystemContext*) MENU;
+(SDLSystemContext*) HMI_OBSCURED;
+(SDLSystemContext*) ALERT;

@end
