//  SDLOnPermissionsChange.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLPermissionItem.h>
#import <SmartDeviceLink/SDLRPCNotification.h>

@interface SDLOnPermissionsChange : SDLRPCNotification

-(id)init;
-(id)initWithDictionary:(NSMutableDictionary *)dict;

@property(assign) NSMutableArray* permissionItem;

@end
