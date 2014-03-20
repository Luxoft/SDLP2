//  SDLTriggerSource.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLTriggerSource.h>

SDLTriggerSource* SDLTriggerSource_TS_MENU = nil;
SDLTriggerSource* SDLTriggerSource_TS_VR = nil;

NSMutableArray* SDLTriggerSource_values = nil;
@implementation SDLTriggerSource

+(SDLTriggerSource*) valueOf:(NSString*) value {
    for (SDLTriggerSource* item in SDLTriggerSource.values) {
        if ([item.value isEqualToString:value]) {
            return item;
        }
    }
    return nil;
}

+(NSMutableArray*) values {
    if (SDLTriggerSource_values == nil) {
        SDLTriggerSource_values = [[NSMutableArray alloc] initWithObjects:
                SDLTriggerSource.TS_MENU,
                SDLTriggerSource.TS_VR,
                nil];
    }
    return SDLTriggerSource_values;
}

+(SDLTriggerSource*) TS_MENU {
    	if (SDLTriggerSource_TS_MENU == nil) {
        		SDLTriggerSource_TS_MENU = [[SDLTriggerSource alloc] initWithValue:@"MENU"];
    	}
    	return SDLTriggerSource_TS_MENU;
}

+(SDLTriggerSource*) TS_VR {
    	if (SDLTriggerSource_TS_VR == nil) {
        		SDLTriggerSource_TS_VR = [[SDLTriggerSource alloc] initWithValue:@"VR"];
    	}
    	return SDLTriggerSource_TS_VR;
}

@end
