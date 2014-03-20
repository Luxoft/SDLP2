//  SDLGlobalProperty.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLGlobalProperty : SDLEnum {}

+(SDLGlobalProperty*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLGlobalProperty*) HELPPROMPT;
+(SDLGlobalProperty*) TIMEOUTPROMPT;
+(SDLGlobalProperty*) VRHELPTITLE;
+(SDLGlobalProperty*) VRHELPITEMS;

@end
