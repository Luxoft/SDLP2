//  SDLAbstractProtocol.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLIProtocol.h>

#import <SmartDeviceLink/SDLITransport.h>

@interface SDLAbstractProtocol : NSObject<SDLIProtocol> {
	NSObject<SDLITransport>* transport;
	NSMutableArray* protocolListeners;
}

@end
