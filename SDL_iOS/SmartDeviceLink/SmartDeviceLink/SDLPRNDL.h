//  SDLPRNDL.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLPRNDL : SDLEnum {}

+(SDLPRNDL*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                 

+(SDLPRNDL*) PARK;
+(SDLPRNDL*) REVERSE;
+(SDLPRNDL*) NEUTRAL;
+(SDLPRNDL*) DRIVE;
+(SDLPRNDL*) SPORT;
+(SDLPRNDL*) LOWGEAR;
+(SDLPRNDL*) FIRST;
+(SDLPRNDL*) SECOND;
+(SDLPRNDL*) THIRD;
+(SDLPRNDL*) FOURTH;
+(SDLPRNDL*) FIFTH;
+(SDLPRNDL*) SIXTH;
+(SDLPRNDL*) SEVENTH;
+(SDLPRNDL*) EIGTH;

@end


