//  SDLPutFile.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLFileType.h>

@interface SDLPutFile : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSString* syncFileName;
@property(assign) SDLFileType* fileType;
@property(assign) NSNumber* persistentFile;

@end
