//  SDLRegisterAppInterfaceResponse.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLRPCResponse.h>

#import <SmartDeviceLink/SDLDisplayCapabilities.h>
#import <SmartDeviceLink/SDLLanguage.h>
#import <SmartDeviceLink/SDLPresetBankCapabilities.h>
#import <SmartDeviceLink/SDLSyncMsgVersion.h>
#import <SmartDeviceLink/SDLVehicleType.h>

@interface SDLRegisterAppInterfaceResponse : SDLRPCResponse {}

-(id) init;
-(id) initWithDictionary:(NSMutableDictionary*) dict;

@property(assign) SDLSyncMsgVersion* syncMsgVersion;
@property(assign) SDLLanguage* language;
@property(assign) SDLLanguage* hmiDisplayLanguage;
@property(assign) SDLDisplayCapabilities* displayCapabilities;
@property(assign) NSMutableArray* buttonCapabilities;
@property(assign) NSMutableArray* softButtonCapabilities;
@property(assign) SDLPresetBankCapabilities* presetBankCapabilities;
@property(assign) NSMutableArray* hmiZoneCapabilities;
@property(assign) NSMutableArray* speechCapabilities;
@property(assign) NSMutableArray* vrCapabilities;
@property(assign) NSMutableArray* audioPassThruCapabilities;
@property(assign) SDLVehicleType* vehicleType;


@end
