//  SDLJsonEncoder.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEncoder.h>

@interface SDLJsonEncoder : NSObject<SDLEncoder> {}

+(NSObject<SDLEncoder>*) instance;

@end
