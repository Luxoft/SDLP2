//  SDLOnButtonEvent.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLOnButtonEvent.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLOnButtonEvent

-(id) init {
    if (self = [super initWithName:NAMES_OnButtonEvent]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setButtonName:(SDLButtonName*) buttonName {
    if (buttonName != nil) {
        [parameters setObject:buttonName forKey:NAMES_buttonName];
    } else {
        [parameters removeObjectForKey:NAMES_buttonName];
    }
}

-(SDLButtonName*) buttonName {
    NSObject* obj = [parameters objectForKey:NAMES_buttonName];
    if ([obj isKindOfClass:SDLButtonName.class]) {
        return (SDLButtonName*)obj;
    } else { 
        return [SDLButtonName valueOf:(NSString*)obj];
    }
}

-(void) setButtonEventMode:(SDLButtonEventMode*) buttonEventMode {
    if (buttonEventMode != nil) {
        [parameters setObject:buttonEventMode forKey:NAMES_buttonEventMode];
    } else {
        [parameters removeObjectForKey:NAMES_buttonEventMode];
    }
}

-(SDLButtonEventMode*) buttonEventMode {
    NSObject* obj = [parameters objectForKey:NAMES_buttonEventMode];
    if ([obj isKindOfClass:SDLButtonEventMode.class]) {
        return (SDLButtonEventMode*)obj;
    } else { 
        return [SDLButtonEventMode valueOf:(NSString*)obj];
    }
}

-(void) setCustomButtonID:(NSNumber *) customButtonID {
    if (customButtonID != nil) {
        [parameters setObject:customButtonID forKey:NAMES_customButtonID];
    } else {
        [parameters removeObjectForKey:NAMES_customButtonID];
    }
}

-(NSNumber*) customButtonID {
    return [parameters objectForKey:NAMES_customButtonID];
}

@end
