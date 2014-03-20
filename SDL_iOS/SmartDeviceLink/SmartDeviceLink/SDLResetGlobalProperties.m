//  SDLResetGlobalProperties.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLResetGlobalProperties.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLResetGlobalProperties

-(id) init {
    if (self = [super initWithName:NAMES_ResetGlobalProperties]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setProperties:(NSMutableArray*) properties {
    if (properties != nil) {
        [parameters setObject:properties forKey:NAMES_properties];
    } else {
        [parameters removeObjectForKey:NAMES_properties];
    }
}

-(NSMutableArray*) properties {
    NSMutableArray* array = [parameters objectForKey:NAMES_properties];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:SDLGlobalProperty.class]) {
        return array;
    } else { 
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSString* enumString in array) {
            [newList addObject:[SDLGlobalProperty valueOf:enumString]];
        }
        return newList;
    }
}

@end
