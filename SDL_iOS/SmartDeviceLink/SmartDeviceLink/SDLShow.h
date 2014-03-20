//  SDLShow.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLImage.h>
#import <SmartDeviceLink/SDLSoftButton.h>
#import <SmartDeviceLink/SDLTextAlignment.h>

@interface SDLShow : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) NSString* mainField1;
@property(assign) NSString* mainField2;
@property(assign) NSString* mainField3;
@property(assign) NSString* mainField4;
@property(assign) SDLTextAlignment* alignment;
@property(assign) NSString* statusBar;
@property(assign) NSString* mediaClock;
@property(assign) NSString* mediaTrack;
@property(assign) SDLImage* graphic;
@property(assign) SDLSoftButton* softButtons;
@property(assign) NSMutableArray* customPresets;

@end
