//  SDLDeleteCommand.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDeleteCommand.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLDeleteCommand

-(id) init {
    if (self = [super initWithName:NAMES_DeleteCommand]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setCmdID:(NSNumber*) cmdID {
    if (cmdID != nil) {
        [parameters setObject:cmdID forKey:NAMES_cmdID];
    } else {
        [parameters removeObjectForKey:NAMES_cmdID];
    }
}

-(NSNumber*) cmdID {
    return [parameters objectForKey:NAMES_cmdID];
}

@end
