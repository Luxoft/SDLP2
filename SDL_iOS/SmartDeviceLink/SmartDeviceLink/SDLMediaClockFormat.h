//  SDLMediaClockFormat.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLMediaClockFormat : SDLEnum {}

+(SDLMediaClockFormat*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLMediaClockFormat*) CLOCK1;
+(SDLMediaClockFormat*) CLOCK2;
+(SDLMediaClockFormat*) CLOCK3;
+(SDLMediaClockFormat*) CLOCKTEXT1;
+(SDLMediaClockFormat*) CLOCKTEXT2;
+(SDLMediaClockFormat*) CLOCKTEXT3;
+(SDLMediaClockFormat*) CLOCKTEXT4;

@end
