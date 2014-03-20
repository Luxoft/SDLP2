//  SDLAppHMIType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLAppHMIType : SDLEnum {}

+(SDLAppHMIType*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLAppHMIType*) DEFAULT;
+(SDLAppHMIType*) COMMUNICATION;
+(SDLAppHMIType*) MEDIA;
+(SDLAppHMIType*) MESSAGING;
+(SDLAppHMIType*) NAVIGATION;
+(SDLAppHMIType*) INFORMATION;
+(SDLAppHMIType*) SOCIAL;
+(SDLAppHMIType*) BACKGROUND_PROCESS;
+(SDLAppHMIType*) TESTING;
+(SDLAppHMIType*) SYSTEM;

@end
