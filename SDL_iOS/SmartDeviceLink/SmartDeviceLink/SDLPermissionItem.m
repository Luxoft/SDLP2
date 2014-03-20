//  SDLPermissionItem.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLPermissionItem.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLPermissionItem

-(id)init {
    if (self = [super init]) {
        
    }
    return self;
}

-(id)initWithDictionary:(NSMutableDictionary *)dict {
    if (self = [super initWithDictionary:dict]) {
        
    }
    return self;
}

-(void) setRpcName:(NSString *) rpcName {
    if (rpcName != nil) {
        [store setObject:rpcName forKey:NAMES_rpcName];
    } else {
        [store removeObjectForKey:NAMES_rpcName];
    }
}

-(NSString*) rpcName {
    return [store objectForKey:NAMES_rpcName];
}

-(void) setHmiPermissions:(NSMutableArray*) hmiPermissions {
    if (hmiPermissions != nil) {
        [store setObject:hmiPermissions forKey:NAMES_hmiPermissions];
    } else {
        [store removeObjectForKey:NAMES_hmiPermissions];
    }
}

-(NSMutableArray*) hmiPermissions {
    NSMutableArray* array = [store objectForKey:NAMES_hmiPermissions];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:SDLHMIPermissions.class]) {
        return array;
    } else {
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSDictionary* dict in array) {
            [newList addObject:[[[SDLHMIPermissions alloc] initWithDictionary:(NSMutableDictionary*)dict] autorelease]];
        }
        return newList;
    }
}

-(void) setParameterPermissions:(NSMutableArray*) parameterPermissions {
    if (parameterPermissions != nil) {
        [store setObject:parameterPermissions forKey:NAMES_parameterPermissions];
    } else {
        [store removeObjectForKey:NAMES_parameterPermissions];
    }
}

-(NSMutableArray*) parameterPermissions {
    NSMutableArray* array = [store objectForKey:NAMES_parameterPermissions];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:SDLParameterPermissions.class]) {
        return array;
    } else {
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSDictionary* dict in array) {
            [newList addObject:[[[SDLParameterPermissions alloc] initWithDictionary:(NSMutableDictionary*)dict] autorelease]];
        }
        return newList;
    }
}

@end
