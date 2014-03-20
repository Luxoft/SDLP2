//  SDLScrollableMessage.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

@interface SDLScrollableMessage : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSString* scrollableMessageBody;
@property(assign) NSNumber* timeout;
@property(assign) NSMutableArray* softButtons;

@end
