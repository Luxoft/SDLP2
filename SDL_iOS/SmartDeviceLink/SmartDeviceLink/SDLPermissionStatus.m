//  SDLPermissionStatus.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLPermissionStatus.h>

SDLPermissionStatus* SDLPermissionStatus_ALLOWED = nil;
SDLPermissionStatus* SDLPermissionStatus_DISALLOWED = nil;
SDLPermissionStatus* SDLPermissionStatus_USER_DISALLOWED = nil;
SDLPermissionStatus* SDLPermissionStatus_USER_CONSENT_PENDING = nil;

NSMutableArray* SDLPermissionStatus_values = nil;

@implementation SDLPermissionStatus

+(SDLPermissionStatus*)valueOf:(NSString *)value {
    for (SDLPermissionStatus *item in SDLPermissionStatus.values) {
        if ([item.value isEqualToString:value]) {
            return item;
        }
    }
    return nil;
}

+(NSMutableArray*)values {
    if (SDLPermissionStatus_values == nil) {
        SDLPermissionStatus_values = [[NSMutableArray alloc] initWithObjects: 
                                     SDLPermissionStatus.ALLOWED,
                                     SDLPermissionStatus.DISALLOWED,
                                     SDLPermissionStatus.USER_DISALLOWED,
                                     SDLPermissionStatus.USER_CONSENT_PENDING,
                                     nil];
    }
    return SDLPermissionStatus_values;
}

+(SDLPermissionStatus*)ALLOWED {
    if (SDLPermissionStatus_ALLOWED == nil) {
        SDLPermissionStatus_ALLOWED = [[SDLPermissionStatus alloc] initWithValue:@"ALLOWED"];
    }
    return SDLPermissionStatus_ALLOWED;
}

+(SDLPermissionStatus*)DISALLOWED {
    if (SDLPermissionStatus_DISALLOWED == nil) {
        SDLPermissionStatus_DISALLOWED = [[SDLPermissionStatus alloc] initWithValue:@"DISALLOWED"];
    }
    return SDLPermissionStatus_DISALLOWED;
}

+(SDLPermissionStatus*)USER_DISALLOWED {
    if (SDLPermissionStatus_USER_DISALLOWED == nil) {
        SDLPermissionStatus_USER_DISALLOWED = [[SDLPermissionStatus alloc] initWithValue:@"USER_DISALLOWED"];
    }
    return SDLPermissionStatus_USER_DISALLOWED;
}

+(SDLPermissionStatus*)USER_CONSENT_PENDING {
    if (SDLPermissionStatus_USER_CONSENT_PENDING == nil) {
        SDLPermissionStatus_USER_CONSENT_PENDING = [[SDLPermissionStatus alloc] initWithValue:@"USER_CONSENT_PENDING"];
    }
    return SDLPermissionStatus_USER_CONSENT_PENDING;
}

@end