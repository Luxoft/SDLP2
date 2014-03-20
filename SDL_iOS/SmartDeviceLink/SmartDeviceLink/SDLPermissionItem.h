//  SDLPermissionItem.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLHMIPermissions.h>
#import <SmartDeviceLink/SDLRPCMessage.h>

#import <SmartDeviceLink/SDLParameterPermissions.h>
#import <SmartDeviceLink/SDLPermissionStatus.h>

@interface SDLPermissionItem : SDLRPCStruct {}

-(id)init;
-(id)initWithDictionary:(NSMutableDictionary *)dict;

@property(assign) NSString* rpcName;
@property(assign) NSMutableArray* hmiPermissions;
@property(assign) NSMutableArray* parameterPermissions;

@end
