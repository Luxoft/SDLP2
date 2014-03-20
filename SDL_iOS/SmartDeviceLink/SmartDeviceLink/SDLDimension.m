//  SDLDimension.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDimension.h>   

SDLDimension* SDLDimension_NO_FIX = nil;
SDLDimension* SDLDimension_2D = nil;
SDLDimension* SDLDimension_3D = nil;

NSMutableArray* SDLDimension_values = nil; 

@implementation SDLDimension

+(SDLDimension*) valueOf:(NSString*) value {                       
	for (SDLDimension* item in SDLDimension.values) {    
		if ([item.value isEqualToString:value]) { 
			return item; 
		} 
	} 
	return nil; 
}

+(NSMutableArray *) values {           
	if (SDLDimension_values == nil) {                               
		SDLDimension_values = [[NSMutableArray alloc] initWithObjects: 
                                    SDLDimension.NO_FIX,
                                    SDLDimension._2D,
                                    SDLDimension._3D,
									nil];                        
	} 
	return SDLDimension_values; 
}

+(SDLDimension*) NO_FIX {
	if (SDLDimension_NO_FIX == nil) {
		SDLDimension_NO_FIX = [[SDLDimension alloc] initWithValue:@"NO_FIX"];
	} 
	return SDLDimension_NO_FIX;  
}

+(SDLDimension*) _2D {
	if (SDLDimension_2D == nil) {
		SDLDimension_2D = [[SDLDimension alloc] initWithValue:@"2D"];
	}
	return SDLDimension_2D;
}

+(SDLDimension*) _3D {
	if (SDLDimension_3D == nil) {
		SDLDimension_3D = [[SDLDimension alloc] initWithValue:@"3D"];
	}
	return SDLDimension_3D;
}

@end


