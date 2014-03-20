//  SDLDeleteInteractionChoiceSet.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDeleteInteractionChoiceSet.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLDeleteInteractionChoiceSet

-(id) init {
    if (self = [super initWithName:NAMES_DeleteInteractionChoiceSet]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setInteractionChoiceSetID:(NSNumber*) interactionChoiceSetID {
    if (interactionChoiceSetID != nil) {
        [parameters setObject:interactionChoiceSetID forKey:NAMES_interactionChoiceSetID];
    } else {
        [parameters removeObjectForKey:NAMES_interactionChoiceSetID];
    }
}

-(NSNumber*) interactionChoiceSetID {
    return [parameters objectForKey:NAMES_interactionChoiceSetID];
}

@end
