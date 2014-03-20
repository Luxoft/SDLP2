//  SDLParameterPermissions.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLParameterPermissions.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLParameterPermissions

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setAllowed:(NSMutableArray *) allowed {
    if (allowed != nil) {
        [store setObject:allowed forKey:NAMES_allowed];
    } else {
        [store removeObjectForKey:NAMES_allowed];
    }
}

-(NSMutableArray*) allowed {
    return [store objectForKey:NAMES_allowed];
}

-(void) setUserDisallowed:(NSMutableArray *) userDisallowed {
    if (userDisallowed != nil) {
        [store setObject:userDisallowed forKey:NAMES_userDisallowed];
    } else {
        [store removeObjectForKey:NAMES_userDisallowed];
    }
}

-(NSMutableArray*) userDisallowed {
    return [store objectForKey:NAMES_userDisallowed];
}

@end
