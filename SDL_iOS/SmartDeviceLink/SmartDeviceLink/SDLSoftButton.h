//  SDLSoftButton.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLSoftButtonType.h>
#import <SmartDeviceLink/SDLImage.h>
#import <SmartDeviceLink/SDLSystemAction.h>

@interface SDLSoftButton : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLSoftButtonType* type;
@property(assign) NSString* text;
@property(assign) SDLImage* image;
@property(assign) NSNumber* isHighlighted;
@property(assign) NSNumber* softButtonID;
@property(assign) SDLSystemAction* systemAction;

@end
