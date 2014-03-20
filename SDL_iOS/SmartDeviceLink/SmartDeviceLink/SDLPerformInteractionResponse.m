//  SDLPerformInteractionResponse.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLPerformInteractionResponse.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLPerformInteractionResponse

-(id) init {
    if (self = [super initWithName:NAMES_PerformInteraction]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setChoiceID:(NSNumber*) choiceID {
    if (choiceID != nil) {
        [parameters setObject:choiceID forKey:NAMES_choiceID];
    } else {
        [parameters removeObjectForKey:NAMES_choiceID];
    }
}

-(NSNumber*) choiceID {
    return [parameters objectForKey:NAMES_choiceID];
}

-(void) setTriggerSource:(SDLTriggerSource*) triggerSource {
    if (triggerSource != nil) {
        [parameters setObject:triggerSource forKey:NAMES_triggerSource];
    } else {
        [parameters removeObjectForKey:NAMES_triggerSource];
    }
}

-(SDLTriggerSource*) triggerSource {
    NSObject* obj = [parameters objectForKey:NAMES_triggerSource];
    if ([obj isKindOfClass:SDLTriggerSource.class]) {
        return (SDLTriggerSource*)obj;
    } else { 
        return [SDLTriggerSource valueOf:(NSString*)obj];
    }
}

@end
