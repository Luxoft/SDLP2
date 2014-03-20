//  SDLSlider.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

@interface SDLSlider : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSNumber* numTicks;
@property(assign) NSNumber* position;
@property(assign) NSString* sliderHeader;
@property(assign) NSMutableArray* sliderFooter;
@property(assign) NSNumber* timeout;

@end
