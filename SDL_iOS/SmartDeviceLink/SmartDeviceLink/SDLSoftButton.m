//  SDLSoftButton.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLSoftButton.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLSoftButton

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setType:(SDLSoftButtonType*) type {
    if (type != nil) {
        [store setObject:type forKey:NAMES_type];
    } else {
        [store removeObjectForKey:NAMES_type];
    }
}

-(SDLSoftButtonType*) type {
    NSObject* obj = [store objectForKey:NAMES_type];
    if ([obj isKindOfClass:SDLSoftButtonType.class]) {
        return (SDLSoftButtonType*)obj;
    } else { 
        return [SDLSoftButtonType valueOf:(NSString*)obj];
    }
}

-(void) setText:(NSString *) text {
    if (text != nil) {
        [store setObject:text forKey:NAMES_text];
    } else {
        [store removeObjectForKey:NAMES_text];
    }
}

-(NSString*) text {
    return [store objectForKey:NAMES_text];
}

-(void) setImage:(SDLImage*) image {
    if (image != nil) {
        [store setObject:image forKey:NAMES_image];
    } else {
        [store removeObjectForKey:NAMES_image];
    }
}

-(SDLImage*) image {
    NSObject* obj = [store objectForKey:NAMES_image];
    if ([obj isKindOfClass:SDLImage.class]) {
        return (SDLImage*)obj;
    } else {
        return [[[SDLImage alloc] initWithDictionary:(NSMutableDictionary*)obj] autorelease];
    }
}

-(void) setIsHighlighted:(NSNumber *) isHighlighted {
    if (isHighlighted != nil) {
        [store setObject:isHighlighted forKey:NAMES_isHighlighted];
    } else {
        [store removeObjectForKey:NAMES_isHighlighted];
    }
}

-(NSNumber*) isHighlighted {
    return [store objectForKey:NAMES_isHighlighted];
}

-(void) setSoftButtonID:(NSNumber *) softButtonID {
    if (softButtonID != nil) {
        [store setObject:softButtonID forKey:NAMES_softButtonID];
    } else {
        [store removeObjectForKey:NAMES_softButtonID];
    }
}

-(NSNumber*) softButtonID {
    return [store objectForKey:NAMES_softButtonID];
}

-(void) setSystemAction:(SDLSystemAction*) systemAction {
    if (systemAction != nil) {
        [store setObject:systemAction forKey:NAMES_systemAction];
    } else {
        [store removeObjectForKey:NAMES_systemAction];
    }
}

-(SDLSystemAction*) systemAction {
    NSObject* obj = [store objectForKey:NAMES_systemAction];
    if ([obj isKindOfClass:SDLSystemAction.class]) {
        return (SDLSystemAction*)obj;
    } else {
        return [SDLSystemAction valueOf:(NSString*)obj];
    }
}

@end
