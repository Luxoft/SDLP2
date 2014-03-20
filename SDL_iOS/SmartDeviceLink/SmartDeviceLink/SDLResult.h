//  SDLResult.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLResult : SDLEnum {}

+(SDLResult*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLResult*) SUCCESS;
+(SDLResult*) INVALID_DATA;
+(SDLResult*) UNSUPPORTED_REQUEST;
+(SDLResult*) OUT_OF_MEMORY;
+(SDLResult*) TOO_MANY_PENDING_REQUESTS;
+(SDLResult*) INVALID_ID;
+(SDLResult*) DUPLICATE_NAME;
+(SDLResult*) TOO_MANY_APPLICATIONS;
+(SDLResult*) APPLICATION_REGISTERED_ALREADY;
+(SDLResult*) UNSUPPORTED_VERSION;
+(SDLResult*) WRONG_LANGUAGE;
+(SDLResult*) APPLICATION_NOT_REGISTERED;
+(SDLResult*) IN_USE;
+(SDLResult*) VEHICLE_DATA_NOT_ALLOWED;
+(SDLResult*) VEHICLE_DATA_NOT_AVAILABLE;
+(SDLResult*) REJECTED;
+(SDLResult*) ABORTED;
+(SDLResult*) IGNORED;
+(SDLResult*) UNSUPPORTED_RESOURCE;
+(SDLResult*) FILE_NOT_FOUND;
+(SDLResult*) GENERIC_ERROR;
+(SDLResult*) DISALLOWED;
+(SDLResult*) USER_DISALLOWED;
+(SDLResult*) TIMED_OUT;
+(SDLResult*) CANCEL_ROUTE;
+(SDLResult*) TRUNCATED_DATA;
+(SDLResult*) RETRY;
+(SDLResult*) WARNINGS;

@end
