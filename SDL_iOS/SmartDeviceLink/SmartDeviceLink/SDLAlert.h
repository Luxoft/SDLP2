//  SDLAlert.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

@interface SDLAlert : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSString* alertText1;
@property(assign) NSString* alertText2;
@property(assign) NSString* alertText3;
@property(assign) NSMutableArray* ttsChunks;
@property(assign) NSNumber* duration;
@property(assign) NSNumber* playTone;
@property(assign) NSMutableArray* softButtons;

@end
