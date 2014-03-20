//  SDLSystemAction.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLSystemAction : SDLEnum {}

+(SDLSystemAction*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLSystemAction*) DEFAULT_ACTION;
+(SDLSystemAction*) STEAL_FOCUS;
+(SDLSystemAction*) KEEP_CONTEXT;

@end
