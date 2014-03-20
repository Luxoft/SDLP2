//  SDLChoice.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLImage.h>

@interface SDLChoice : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* choiceID;
@property(assign) NSString* menuName;
@property(assign) NSMutableArray* vrCommands;
@property(assign) SDLImage* image;

@end
