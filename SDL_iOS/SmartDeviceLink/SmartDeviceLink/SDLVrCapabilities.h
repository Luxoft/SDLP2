//  SDLVrCapabilities.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLVrCapabilities : SDLEnum {}

+(SDLVrCapabilities*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLVrCapabilities*) TEXT;

@end
