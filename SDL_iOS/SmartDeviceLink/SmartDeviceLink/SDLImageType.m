//  SDLImageType.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLImageType.h>

SDLImageType* SDLImageType_STATIC = nil;
SDLImageType* SDLImageType_DYNAMIC = nil;

NSMutableArray* SDLImageType_values = nil;
@implementation SDLImageType

+(SDLImageType*) valueOf:(NSString*) imageType {
    for (SDLImageType* item in SDLImageType.values) {
        if ([item.value isEqualToString:imageType]) {
            return item;
        }
    }
    return nil;
}

+(NSMutableArray*) values {
    if (SDLImageType_values == nil) {
        SDLImageType_values = [[NSMutableArray alloc] initWithObjects:
                SDLImageType.STATIC,
                SDLImageType.DYNAMIC,
                nil];
    }
    return SDLImageType_values;
}

+(SDLImageType*) STATIC {
    	if (SDLImageType_STATIC == nil) {
        		SDLImageType_STATIC = [[SDLImageType alloc] initWithValue:@"STATIC"];
    	}
    	return SDLImageType_STATIC;
}

+(SDLImageType*) DYNAMIC {
    	if (SDLImageType_DYNAMIC == nil) {
        		SDLImageType_DYNAMIC = [[SDLImageType alloc] initWithValue:@"DYNAMIC"];
    	}
    	return SDLImageType_DYNAMIC;
}

@end
