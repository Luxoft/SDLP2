//  SDLAbstractProtocol.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLAbstractProtocol.h>

@implementation SDLAbstractProtocol

-(void) sendStartSessionWithType:(SDLSessionType) sessionType {
	[self doesNotRecognizeSelector:_cmd];
}

-(void) sendEndSessionWithType:(SDLSessionType)sessionType sessionID:(Byte)sessionID {
	[self doesNotRecognizeSelector:_cmd];
}

-(void) sendData:(SDLProtocolMessage*) protocolMsg {
    [self doesNotRecognizeSelector:_cmd];
}

-(void) handleBytesFromTransport:(Byte *)receivedBytes length:(long)receivedBytesLength {
	[self doesNotRecognizeSelector:_cmd];
}

-(id) init {
	if (self = [super init]) {
		protocolListeners = [[NSMutableArray alloc] initWithCapacity:1];
	}
	return self;
}

-(void) addProtocolListener:(NSObject<SDLProtocolListener>*)listener {
	@synchronized (protocolListeners) {
		[protocolListeners addObject:listener];
	}
}	

-(void) removeProtocolListener:(NSObject<SDLProtocolListener>*)listener {
	@synchronized (protocolListeners) {
		[protocolListeners removeObject:listener];
	}
}

-(void) setTransport:(NSObject<SDLITransport>*) theTransport {
	transport = theTransport;
}

-(NSObject<SDLITransport>*) transport {
	return transport;
}

- (void) onTransportConnected {
	NSArray* localListeners = nil;
	
	@synchronized(protocolListeners) {
		localListeners = [protocolListeners copy];
	}
	
	for (NSObject<SDLProtocolListener>* listener in localListeners) {
		
		[listener onProtocolOpened];
	}
	[localListeners release];
}

- (void) onTransportDisconnected {
    NSLog(@"%@", NSStringFromSelector(_cmd));
    NSArray* localListeners = nil;
	@synchronized(protocolListeners) {
		localListeners = [protocolListeners copy];
	}
	
	for (NSObject<SDLProtocolListener>* listener in localListeners) {
		
		[listener onProtocolClosed];
	}
	[localListeners release];
}

- (void) onBytesReceived:(Byte*)bytes length:(long) length {
	[self handleBytesFromTransport:(Byte*)bytes length:length];
}

-(void) dealloc {
	[protocolListeners release];
	[super dealloc];
}

@end
