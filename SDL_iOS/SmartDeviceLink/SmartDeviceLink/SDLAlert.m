//  SDLAlert.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLAlert.h>

#import <SmartDeviceLink/SDLNames.h>
#import <SmartDeviceLink/SDLSoftButton.h>
#import <SmartDeviceLink/SDLTTSChunk.h>

@implementation SDLAlert

-(id) init {
    if (self = [super initWithName:NAMES_Alert]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setAlertText1:(NSString*) alertText1 {
    if (alertText1 != nil) {
        [parameters setObject:alertText1 forKey:NAMES_alertText1];
    } else {
        [parameters removeObjectForKey:NAMES_alertText1];
    }
}

-(NSString*) alertText1 {
    return [parameters objectForKey:NAMES_alertText1];
}

-(void) setAlertText2:(NSString*) alertText2 {
    if (alertText2 != nil) {
        [parameters setObject:alertText2 forKey:NAMES_alertText2];
    } else {
        [parameters removeObjectForKey:NAMES_alertText2];
    }
}

-(NSString*) alertText2 {
    return [parameters objectForKey:NAMES_alertText2];
}

-(void) setAlertText3:(NSString*) alertText3 {
    if (alertText3 != nil) {
        [parameters setObject:alertText3 forKey:NAMES_alertText3];
    } else {
        [parameters removeObjectForKey:NAMES_alertText3];
    }
}

-(NSString*) alertText3 {
    return [parameters objectForKey:NAMES_alertText3];
}

-(void) setTtsChunks:(NSMutableArray*) ttsChunks {
    if (ttsChunks != nil) {
        [parameters setObject:ttsChunks forKey:NAMES_ttsChunks];
    } else {
        [parameters removeObjectForKey:NAMES_ttsChunks];
    }
}

-(NSMutableArray*) ttsChunks {
    NSMutableArray* array = [parameters objectForKey:NAMES_ttsChunks];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:SDLTTSChunk.class]) {
        return array;
    } else {
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSDictionary* dict in array) {
            [newList addObject:[[[SDLTTSChunk alloc] initWithDictionary:(NSMutableDictionary*)dict] autorelease]];
        }
        return newList;
    }
}

-(void) setDuration:(NSNumber*) duration {
    if (duration != nil) {
        [parameters setObject:duration forKey:NAMES_duration];
    } else {
        [parameters removeObjectForKey:NAMES_duration];
    }
}

-(NSNumber*) duration {
    return [parameters objectForKey:NAMES_duration];
}

-(void) setPlayTone:(NSNumber*) playTone {
    if (playTone != nil) {
        [parameters setObject:playTone forKey:NAMES_playTone];
    } else {
        [parameters removeObjectForKey:NAMES_playTone];
    }
}

-(NSNumber*) playTone {
    return [parameters objectForKey:NAMES_playTone];
}

-(void) setSoftButtons:(NSMutableArray *) softButtons {
    if (softButtons != nil) {
        [parameters setObject:softButtons forKey:NAMES_softButtons];
    } else {
        [parameters removeObjectForKey:NAMES_softButtons];
    }
}

-(NSMutableArray*) softButtons {
    NSMutableArray* array = [parameters objectForKey:NAMES_softButtons];
    if ([array count] < 1 || [[array objectAtIndex:0] isKindOfClass:SDLSoftButton.class]) {
        return array;
    } else {
        NSMutableArray* newList = [NSMutableArray arrayWithCapacity:[array count]];
        for (NSDictionary* dict in array) {
            [newList addObject:[[[SDLSoftButton alloc] initWithDictionary:(NSMutableDictionary*)dict] autorelease]];
        }
        return newList;
    }
}

@end
