//  SDLPutFile.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLPutFile.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLPutFile

-(id) init {
    if (self = [super initWithName:NAMES_PutFile]) {}
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

-(void) setFileType:(SDLFileType *) fileType {
    if (fileType != nil) {
        [parameters setObject:fileType forKey:NAMES_fileType];
    } else {
        [parameters removeObjectForKey:NAMES_fileType];
    }
}

-(SDLFileType*) fileType {
    NSObject* obj = [parameters objectForKey:NAMES_fileType];
    if ([obj isKindOfClass:SDLFileType.class]) {
        return (SDLFileType*)obj;
    } else {
        return [SDLFileType valueOf:(NSString*)obj];
    }
}

-(void) setPersistentFile:(NSNumber *) persistentFile {
    if (persistentFile != nil) {
        [parameters setObject:persistentFile forKey:NAMES_persistentFile];
    } else {
        [parameters removeObjectForKey:NAMES_persistentFile];
    }
}

-(NSNumber*) persistentFile {
    return [parameters objectForKey:NAMES_persistentFile];
}


@end
