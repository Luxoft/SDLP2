//  SDLButtonPressMode.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLButtonPressMode : SDLEnum {}

+(SDLButtonPressMode*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLButtonPressMode*) LONG;
+(SDLButtonPressMode*) SHORT;

@end
