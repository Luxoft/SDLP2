//  SDLOnButtonEvent.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCNotification.h>

#import <SmartDeviceLink/SDLButtonEventMode.h>
#import <SmartDeviceLink/SDLButtonName.h>

@interface SDLOnButtonEvent : SDLRPCNotification {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLButtonName* buttonName;
@property(assign) SDLButtonEventMode* buttonEventMode;
@property(assign) NSNumber* customButtonID;

@end
