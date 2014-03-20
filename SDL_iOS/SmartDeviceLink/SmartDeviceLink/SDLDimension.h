//  SDLDimension.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLDimension : SDLEnum {}

+(SDLDimension*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                 

+(SDLDimension*) NO_FIX;
+(SDLDimension*) _2D;
+(SDLDimension*) _3D;

@end


