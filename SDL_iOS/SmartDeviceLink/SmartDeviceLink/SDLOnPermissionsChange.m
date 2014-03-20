//  SDLOnPermissionsChange.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLOnPermissionsChange.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLOnPermissionsChange

-(id) init {
    if (self = [super initWithName:NAMES_OnPermissionsChange]) {
        
    }
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary *)dict {
    if (self = [super initWithDictionary:dict]) {
        
    }
    return self;
}

-(void) setPermissionItem:(NSMutableArray *)permissionItem {
    if (permissionItem != nil) {
        [parameters setObject:permissionItem forKey:NAMES_PermissionItem];
    } else {
        [parameters removeObjectForKey:NAMES_PermissionItem];
    }
}

-(NSMutableArray*) permissionItem {
    return [parameters objectForKey:NAMES_PermissionItem];
}

@end
