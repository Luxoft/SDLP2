//  SDLTextAlignment.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLTextAlignment : SDLEnum {}

+(SDLTextAlignment*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLTextAlignment*) LEFT_ALIGNED;
+(SDLTextAlignment*) RIGHT_ALIGNED;
+(SDLTextAlignment*) CENTERED;

@end
