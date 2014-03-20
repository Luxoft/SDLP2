//  SDLTTSChunk.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLTTSChunk.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLTTSChunk

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setText:(NSString*) text {
    if (text != nil) {
        [store setObject:text forKey:NAMES_text];
    } else {
        [store removeObjectForKey:NAMES_text];
    }
}

-(NSString*) text {
    return [store objectForKey:NAMES_text];
}

-(void) setType:(SDLSpeechCapabilities*) type {
    if (type != nil) {
        [store setObject:type forKey:NAMES_type];
    } else {
        [store removeObjectForKey:NAMES_type];
    }
}

-(SDLSpeechCapabilities*) type {
    NSObject* obj = [store objectForKey:NAMES_type];
    if ([obj isKindOfClass:SDLSpeechCapabilities.class]) {
        return (SDLSpeechCapabilities*)obj;
    } else { 
        return [SDLSpeechCapabilities valueOf:(NSString*)obj];
    }
}

@end
