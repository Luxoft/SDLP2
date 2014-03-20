//  SDLButtonEventMode.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLButtonEventMode : SDLEnum {}

+(SDLButtonEventMode*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLButtonEventMode*) BUTTONUP;
+(SDLButtonEventMode*) BUTTONDOWN;

@end
