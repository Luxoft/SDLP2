//  SDLSetDisplayLayout.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSetDisplayLayout.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSetDisplayLayout

-(id) init {
    if (self = [super initWithName:NAMES_SetDisplayLayout]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setDisplayLayout:(NSString *) displayLayout {
    if (displayLayout != nil) {
        [parameters setObject:displayLayout forKey:NAMES_displayLayout];
    } else {
        [parameters removeObjectForKey:NAMES_displayLayout];
    }
}

-(NSString*) displayLayout {
    return [parameters objectForKey:NAMES_displayLayout];
}

@end
