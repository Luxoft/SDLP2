//  SDLGlobalProperty.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLGlobalProperty.h>

SDLGlobalProperty* SDLGlobalProperty_HELPPROMPT = nil;
SDLGlobalProperty* SDLGlobalProperty_TIMEOUTPROMPT = nil;
SDLGlobalProperty* SDLGlobalProperty_VRHELPTITLE = nil;
SDLGlobalProperty* SDLGlobalProperty_VRHELPITEMS = nil;

NSMutableArray* SDLGlobalProperty_values = nil;
@implementation SDLGlobalProperty

+(SDLGlobalProperty*) valueOf:(NSString*) value {
    for (SDLGlobalProperty* item in SDLGlobalProperty.values) {
        if ([item.value isEqualToString:value]) {
            return item;
        }
    }
    return nil;
}

+(NSMutableArray*) values {
    if (SDLGlobalProperty_values == nil) {
        SDLGlobalProperty_values = [[NSMutableArray alloc] initWithObjects:
                SDLGlobalProperty.HELPPROMPT,
                SDLGlobalProperty.TIMEOUTPROMPT,
                nil];
    }
    return SDLGlobalProperty_values;
}

+(SDLGlobalProperty*) HELPPROMPT {
    	if (SDLGlobalProperty_HELPPROMPT == nil) {
        		SDLGlobalProperty_HELPPROMPT = [[SDLGlobalProperty alloc] initWithValue:@"HELPPROMPT"];
    	}
    	return SDLGlobalProperty_HELPPROMPT;
}

+(SDLGlobalProperty*) TIMEOUTPROMPT {
    	if (SDLGlobalProperty_TIMEOUTPROMPT == nil) {
        		SDLGlobalProperty_TIMEOUTPROMPT = [[SDLGlobalProperty alloc] initWithValue:@"TIMEOUTPROMPT"];
    	}
    	return SDLGlobalProperty_TIMEOUTPROMPT;
}

+(SDLGlobalProperty*) VRHELPTITLE {
    if (SDLGlobalProperty_VRHELPTITLE == nil) {
        SDLGlobalProperty_VRHELPTITLE = [[SDLGlobalProperty alloc] initWithValue:@"VRHELPTITLE"];
    }
    return SDLGlobalProperty_VRHELPTITLE;
}

+(SDLGlobalProperty*) VRHELPITEMS {
    if (SDLGlobalProperty_VRHELPITEMS == nil) {
        SDLGlobalProperty_VRHELPITEMS = [[SDLGlobalProperty alloc] initWithValue:@"VRHELPITEMS"];
    }
    return SDLGlobalProperty_VRHELPITEMS;
}

@end
