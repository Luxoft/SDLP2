//  SDLVehicleDataResult.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLVehicleDataResult.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLVehicleDataResult

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setDataType:(SDLVehicleDataType*) dataType {
    if (dataType != nil) {
        [store setObject:dataType forKey:NAMES_dataType];
    } else {
        [store removeObjectForKey:NAMES_dataType];
    }
}

-(SDLVehicleDataType*) dataType {
    return [store objectForKey:NAMES_dataType];
}

-(void) setResultCode:(SDLVehicleDataResultCode*) resultCode {
    if (resultCode != nil) {
        [store setObject:resultCode forKey:NAMES_resultCode];
    } else {
        [store removeObjectForKey:NAMES_resultCode];
    }
}

-(SDLVehicleDataResultCode*) resultCode {
    return [store objectForKey:NAMES_resultCode];
}

@end
