//  SDLSetGlobalProperties.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

@interface SDLSetGlobalProperties : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSMutableArray* helpPrompt;
@property(assign) NSMutableArray* timeoutPrompt;
@property(assign) NSString* vrHelpTitle;
@property(assign) NSMutableArray* vrHelp;

@end
