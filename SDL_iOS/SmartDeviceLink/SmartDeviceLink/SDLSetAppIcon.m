//  SDLSetAppIcon.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSetAppIcon.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSetAppIcon

-(id) init {
    if (self = [super initWithName:NAMES_SetAppIcon]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setSyncFileName:(NSString *) syncFileName {
    if (syncFileName != nil) {
        [parameters setObject:syncFileName forKey:NAMES_syncFileName];
    } else {
        [parameters removeObjectForKey:NAMES_syncFileName];
    }
}

-(NSString*) syncFileName {
    return [parameters objectForKey:NAMES_syncFileName];
}

@end
