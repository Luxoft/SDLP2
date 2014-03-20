//  SDLRPCMessageType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLRPCMessageType : SDLEnum {}

+(SDLRPCMessageType*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLRPCMessageType*) request;
+(SDLRPCMessageType*) response;
+(SDLRPCMessageType*) notification;

@end
