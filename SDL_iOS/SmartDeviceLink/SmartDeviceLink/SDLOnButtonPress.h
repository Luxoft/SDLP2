//  SDLOnButtonPress.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCNotification.h>

#import <SmartDeviceLink/SDLButtonName.h>
#import <SmartDeviceLink/SDLButtonPressMode.h>

@interface SDLOnButtonPress : SDLRPCNotification {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLButtonName* buttonName;
@property(assign) SDLButtonPressMode* buttonPressMode;
@property(assign) NSNumber* customButtonID;

@end
