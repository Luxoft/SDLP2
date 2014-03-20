//  SDLDisplayCapabilities.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDisplayCapabilities.h>

#import <SmartDeviceLink/SDLMediaClockFormat.h>
#import <SmartDeviceLink/SDLNames.h>
#import <SmartDeviceLink/SDLTextField.h>

@implementation SDLDisplayCapabilities

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setDisplayType:(SDLDisplayType*) displayType {
    if (displayType != nil) {
        [store setObject:displayType forKey:NAMES_displayType];
    } else {
        [store removeObjectForKey:NAMES_displayType];
    }
}

-(SDLDisplayType*) displayType {
    NSObject* obj = [store objectForKey:NAMES_displayType];
    if ([obj isKindOfClass:SDLDisplayType.class]) {
        return (SDLDisplayType*)obj;
    } else { 
        return [SDLDisplayType valueOf:(NSString*)obj];
    }
}

-(void) setTextFields:(NSMutableArray*) textFields {
    if (textFields != nil) {
        [store setObject:textFields forKey:NAMES_textFields];
    } else {
        [store removeObjectForKey:NAMES_textFields];
    }
}

-(NSMutableArray*) textFields {
    NSMutableArray* array = [store objectForKey:NAMES_textFields];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:SDLTextField.class]) {
        return array;
    } else {
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSDictionary* dict in array) {
            [newList addObject:[[[SDLTextField alloc] initWithDictionary:(NSMutableDictionary*)dict] autorelease]];
        }
        return newList;
    }
}

-(void) setMediaClockFormats:(NSMutableArray*) mediaClockFormats {
    if (mediaClockFormats != nil) {
        [store setObject:mediaClockFormats forKey:NAMES_mediaClockFormats];
    } else {
        [store removeObjectForKey:NAMES_mediaClockFormats];
    }
}

-(NSMutableArray*) mediaClockFormats {
    NSMutableArray* array = [store objectForKey:NAMES_mediaClockFormats];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:SDLMediaClockFormat.class]) {
        return array;
    } else { 
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSString* enumString in array) {
            [newList addObject:[SDLMediaClockFormat valueOf:enumString]];
        }
        return newList;
    }
}

-(void) setGraphicSupported:(NSNumber*) graphicSupported {
    if (graphicSupported != nil) {
        [store setObject:graphicSupported forKey:NAMES_graphicSupported];
    } else {
        [store removeObjectForKey:NAMES_graphicSupported];
    }
}

-(NSNumber*) graphicSupported {
    return [store objectForKey:NAMES_graphicSupported];
}

@end
