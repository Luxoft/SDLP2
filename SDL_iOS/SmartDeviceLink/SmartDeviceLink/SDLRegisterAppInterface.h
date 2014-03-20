//  SDLRegisterAppInterface.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCRequest.h>

#import <SmartDeviceLink/SDLLanguage.h>
#import <SmartDeviceLink/SDLSyncMsgVersion.h>

@interface SDLRegisterAppInterface : SDLRPCRequest {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLSyncMsgVersion* syncMsgVersion;
@property(assign) NSString* appName;
@property(assign) NSMutableArray* ttsName;
@property(assign) NSString* ngnMediaScreenAppName;
@property(assign) NSMutableArray* vrSynonyms;
@property(assign) NSNumber* isMediaApplication;
@property(assign) SDLLanguage* languageDesired;
@property(assign) SDLLanguage* hmiDisplayLanguageDesired;
@property(assign) NSMutableArray* appHMIType;
@property(assign) NSString* appID;

@end
