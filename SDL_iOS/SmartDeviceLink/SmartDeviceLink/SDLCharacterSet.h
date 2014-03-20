//  SDLCharacterSet.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLCharacterSet : SDLEnum {}

+(SDLCharacterSet*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLCharacterSet*) TYPE2SET;
+(SDLCharacterSet*) TYPE5SET;
+(SDLCharacterSet*) CID1SET;
+(SDLCharacterSet*) CID2SET;

@end
