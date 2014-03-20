//  SDLVehicleDataResult.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLVehicleDataType.h>
#import <SmartDeviceLink/SDLVehicleDataResultCode.h>

@interface SDLVehicleDataResult : SDLRPCStruct {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLVehicleDataType* dataType;
@property(assign) SDLVehicleDataResultCode* resultCode;

@end
