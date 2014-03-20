//  SDLJsonDecoder.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLJsonDecoder.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLJsonDecoder

static NSObject<SDLDecoder>* jsonDecoderInstance;

+(NSObject<SDLDecoder>*) instance {
	if (jsonDecoderInstance == nil) {
		jsonDecoderInstance = [[SDLJsonDecoder alloc] init];
	}
	return jsonDecoderInstance;
}

-(NSDictionary*) decode:(NSData*) msgBytes{
	NSError* error;
    NSDictionary* jsonObject = [NSJSONSerialization JSONObjectWithData:msgBytes options:kNilOptions error:&error];
    
	return jsonObject;
}

@end
