//  SDLPermissionStatus.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLEnum.h>

@interface SDLPermissionStatus : SDLEnum {}

+(SDLPermissionStatus*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                

+(SDLPermissionStatus*) ALLOWED; 
+(SDLPermissionStatus*) DISALLOWED;
+(SDLPermissionStatus*) USER_DISALLOWED; 
+(SDLPermissionStatus*) USER_CONSENT_PENDING;

@end
