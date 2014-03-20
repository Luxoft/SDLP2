//  SDLPresetBankCapabilities.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLPresetBankCapabilities.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLPresetBankCapabilities

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setOnScreenPresetsAvailable:(NSNumber*) onScreenPresetsAvailable {
    if (onScreenPresetsAvailable != nil) {
        [store setObject:onScreenPresetsAvailable forKey:NAMES_onScreenPresetsAvailable];
    } else {
        [store removeObjectForKey:NAMES_onScreenPresetsAvailable];
    }
}

-(NSNumber*) onScreenPresetsAvailable {
    return [store objectForKey:NAMES_onScreenPresetsAvailable];
}

@end
