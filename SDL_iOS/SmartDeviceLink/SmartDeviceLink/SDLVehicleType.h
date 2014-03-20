//  SDLVehicleType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

@interface SDLVehicleType : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSString* make;
@property(assign) NSString* model;
@property(assign) NSString* modelYear;
@property(assign) NSString* trim;

@end
