//  SDLTriggerSource.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLTriggerSource : SDLEnum {}

+(SDLTriggerSource*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLTriggerSource*) TS_MENU;
+(SDLTriggerSource*) TS_VR;

@end
