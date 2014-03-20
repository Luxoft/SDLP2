//  SDLSoftButtonType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLSoftButtonType : SDLEnum {}

+(SDLSoftButtonType*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLSoftButtonType*) TEXT;
+(SDLSoftButtonType*) IMAGE;
+(SDLSoftButtonType*) BOTH;

@end
