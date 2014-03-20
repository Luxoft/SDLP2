//  SDLImageType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLImageType : SDLEnum {}

+(SDLImageType*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLImageType*) STATIC;
+(SDLImageType*) DYNAMIC;

@end
