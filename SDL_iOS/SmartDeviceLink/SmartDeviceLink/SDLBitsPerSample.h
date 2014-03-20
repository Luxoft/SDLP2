//  SDLBitsPerSample.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLBitsPerSample : SDLEnum {}

+(SDLBitsPerSample*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLBitsPerSample*) _8_BIT;
+(SDLBitsPerSample*) _16_BIT;

@end
