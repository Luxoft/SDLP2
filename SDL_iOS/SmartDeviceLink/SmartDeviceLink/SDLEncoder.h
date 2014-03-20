//  SDLEncoder.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@protocol SDLEncoder

-(NSData*) encodeDictionary:(NSDictionary*) dict;

@end
