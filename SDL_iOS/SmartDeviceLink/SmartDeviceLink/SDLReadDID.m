//  SDLReadDID.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLReadDID.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLReadDID

-(id) init {
    if (self = [super initWithName:NAMES_ReadDID]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setEcuName:(NSNumber *) ecuName {
    if (ecuName != nil) {
        [parameters setObject:ecuName forKey:NAMES_ecuName];
    } else {
        [parameters removeObjectForKey:NAMES_ecuName];
    }
}

-(NSNumber*) ecuName {
    return [parameters objectForKey:NAMES_ecuName];
}

-(void) setDidLocation:(NSMutableArray*) didLocation {
    if (didLocation != nil) {
        [parameters setObject:didLocation forKey:NAMES_didLocation];
    } else {
        [parameters removeObjectForKey:NAMES_didLocation];
    }
}

-(NSMutableArray*) didLocation {
    NSMutableArray* array = [parameters objectForKey:NAMES_didLocation];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:NSNumber.class]) {
        return array;
    } else {
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSNumber* enumNumber in array) {
            [newList addObject:enumNumber];
        }
        return newList;
    }
}


@end
