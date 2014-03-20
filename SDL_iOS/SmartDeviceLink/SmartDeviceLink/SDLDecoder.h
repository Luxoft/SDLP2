//  SDLDecoder.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@protocol SDLDecoder

-(NSDictionary*) decode:(NSData*) msgBytes;

@end
