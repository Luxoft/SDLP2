//  SDLHeadLampStatus.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLHeadLampStatus.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLHeadLampStatus

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setLowBeamsOn:(NSNumber *) lowBeamsOn {
    if (lowBeamsOn != nil) {
        [store setObject:lowBeamsOn forKey:NAMES_lowBeamsOn];
    } else {
        [store removeObjectForKey:NAMES_lowBeamsOn];
    }
}

-(NSNumber*) lowBeamsOn {
    return [store objectForKey:NAMES_lowBeamsOn];
}

-(void) setHighBeamsOn:(NSNumber *) highBeamsOn {
    if (highBeamsOn != nil) {
        [store setObject:highBeamsOn forKey:NAMES_highBeamsOn];
    } else {
        [store removeObjectForKey:NAMES_highBeamsOn];
    }
}

-(NSNumber*) highBeamsOn {
    return [store objectForKey:NAMES_highBeamsOn];
}

@end
