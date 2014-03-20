//  SDLJsonEncoder.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLJsonEncoder.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLJsonEncoder

static NSObject<SDLEncoder>* jsonEncoderInstance;

+(NSObject<SDLEncoder>*) instance {
	if (jsonEncoderInstance == nil) {
		jsonEncoderInstance = [[SDLJsonEncoder alloc] init];
	}
	return jsonEncoderInstance;
}

-(NSData*) encodeDictionary:(NSDictionary*) dict {
    NSError* error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:dict options:kNilOptions error:&error];
    
    return jsonData;
}

@end
