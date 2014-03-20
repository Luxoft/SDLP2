//  SDLAddCommand.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLImage.h>
#import <SmartDeviceLink/SDLMenuParams.h>

@interface SDLAddCommand : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* cmdID;
@property(assign) SDLMenuParams* menuParams;
@property(assign) NSMutableArray* vrCommands;
@property(assign) SDLImage* cmdIcon;

@end
