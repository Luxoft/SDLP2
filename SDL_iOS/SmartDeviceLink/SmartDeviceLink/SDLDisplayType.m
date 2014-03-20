//  SDLDisplayType.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDisplayType.h>

SDLDisplayType* SDLDisplayType_CID = nil;
SDLDisplayType* SDLDisplayType_TYPE2 = nil;
SDLDisplayType* SDLDisplayType_TYPE5 = nil;
SDLDisplayType* SDLDisplayType_NGN = nil;
SDLDisplayType* SDLDisplayType_GEN2_4_DMA = nil;
SDLDisplayType* SDLDisplayType_GEN2_8_DMA = nil;
SDLDisplayType* SDLDisplayType_GEN2_4_HUD = nil;
SDLDisplayType* SDLDisplayType_MFD3 = nil;
SDLDisplayType* SDLDisplayType_MFD4 = nil;
SDLDisplayType* SDLDisplayType_MFD5 = nil;


NSMutableArray* SDLDisplayType_values = nil;
@implementation SDLDisplayType

+(SDLDisplayType*) valueOf:(NSString*) value {
    for (SDLDisplayType* item in SDLDisplayType.values) {
        if ([item.value isEqualToString:value]) {
            return item;
        }
    }
    return nil;
}

+(NSMutableArray*) values {
    if (SDLDisplayType_values == nil) {
        SDLDisplayType_values = [[NSMutableArray alloc] initWithObjects:
                SDLDisplayType.CID,
                SDLDisplayType.TYPE2,
                SDLDisplayType.TYPE5,
                SDLDisplayType.NGN,
                SDLDisplayType.GEN2_4_DMA,
                SDLDisplayType.GEN2_8_DMA,
                SDLDisplayType.GEN2_4_HUD,
                SDLDisplayType.MFD3,
                SDLDisplayType.MFD4,
                SDLDisplayType.MFD5,
                nil];
    }
    return SDLDisplayType_values;
}

+(SDLDisplayType*) CID {
    	if (SDLDisplayType_CID == nil) {
        		SDLDisplayType_CID = [[SDLDisplayType alloc] initWithValue:@"CID"];
    	}
    	return SDLDisplayType_CID;
}

+(SDLDisplayType*) TYPE2 {
    	if (SDLDisplayType_TYPE2 == nil) {
        		SDLDisplayType_TYPE2 = [[SDLDisplayType alloc] initWithValue:@"TYPE2"];
    	}
    	return SDLDisplayType_TYPE2;
}

+(SDLDisplayType*) TYPE5 {
    	if (SDLDisplayType_TYPE5 == nil) {
        		SDLDisplayType_TYPE5 = [[SDLDisplayType alloc] initWithValue:@"TYPE5"];
    	}
    	return SDLDisplayType_TYPE5;
}

+(SDLDisplayType*) NGN {
    	if (SDLDisplayType_NGN == nil) {
        		SDLDisplayType_NGN = [[SDLDisplayType alloc] initWithValue:@"NGN"];
    	}
    	return SDLDisplayType_NGN;
}

+(SDLDisplayType*) GEN2_4_DMA {
    	if (SDLDisplayType_GEN2_4_DMA == nil) {
        		SDLDisplayType_GEN2_4_DMA = [[SDLDisplayType alloc] initWithValue:@"GEN2_4_DMA"];
    	}
    	return SDLDisplayType_GEN2_4_DMA;
}

+(SDLDisplayType*) GEN2_8_DMA {
    	if (SDLDisplayType_GEN2_8_DMA == nil) {
        		SDLDisplayType_GEN2_8_DMA = [[SDLDisplayType alloc] initWithValue:@"GEN2_8_DMA"];
    	}
    	return SDLDisplayType_GEN2_8_DMA;
}

+(SDLDisplayType*) GEN2_4_HUD {
    	if (SDLDisplayType_GEN2_4_HUD == nil) {
        		SDLDisplayType_GEN2_4_HUD = [[SDLDisplayType alloc] initWithValue:@"GEN2_4_HUD"];
    	}
    	return SDLDisplayType_GEN2_4_HUD;
}

+(SDLDisplayType*) MFD3 {
    if (SDLDisplayType_MFD3 == nil) {
        SDLDisplayType_MFD3 = [[SDLDisplayType alloc] initWithValue:@"MFD3"];
    }
    return SDLDisplayType_MFD3;
}

+(SDLDisplayType*) MFD4 {
    if (SDLDisplayType_MFD4 == nil) {
        SDLDisplayType_MFD4 = [[SDLDisplayType alloc] initWithValue:@"MFD4"];
    }
    return SDLDisplayType_MFD4;
}

+(SDLDisplayType*) MFD5 {
    if (SDLDisplayType_MFD5 == nil) {
        SDLDisplayType_MFD5 = [[SDLDisplayType alloc] initWithValue:@"MFD5"];
    }
    return SDLDisplayType_MFD5;
}

@end
