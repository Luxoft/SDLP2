//  SDLBitConverter.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@interface SDLBitConverter : NSObject{}

+(NSData*) intToByteArray:(UInt32) value;
+(UInt32) intFromByteArray:(Byte*) sizeBuf offset:(UInt32) offset;

@end
