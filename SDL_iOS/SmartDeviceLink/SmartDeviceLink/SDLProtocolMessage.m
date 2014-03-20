//  SDLProtocolMessage.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLProtocolMessage.h>

@implementation SDLProtocolMessage

-(id) init {
	if (self = [super init]) {
        [self set_version: 1];
        [self set_sessionType: SDLSessionType_RPC];
        [self set_messageType: SDLMessageType_UNDEFINED];
	}
	return self;
}

-(void) dealloc {
    [__data release];
    [__bulkData release];
    [super dealloc];
}
				
@end
