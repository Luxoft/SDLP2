//  SDLShowConstantTBT.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLImage.h>

@interface SDLShowConstantTBT : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSString* navigationText1;
@property(assign) NSString* navigationText2;
@property(assign) NSString* eta;
@property(assign) NSString* totalDistance;
@property(assign) SDLImage* turnIcon;
@property(assign) NSNumber* distanceToManeuver;
@property(assign) NSNumber* distanceToManeuverScale;
@property(assign) NSNumber* maneuverComplete;
@property(assign) NSMutableArray* softButtons;

@end
